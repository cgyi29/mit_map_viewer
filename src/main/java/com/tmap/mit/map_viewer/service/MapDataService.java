package com.tmap.mit.map_viewer.service;

import com.tmap.mit.map_viewer.cd.ShapeType;
import com.tmap.mit.map_viewer.constant.FileConstant;
import com.tmap.mit.map_viewer.constant.ShpHeader;
import com.tmap.mit.map_viewer.constant.ShpRecordContent;
import com.tmap.mit.map_viewer.constant.ShpRecordHeader;
import com.tmap.mit.map_viewer.dto.BoundingBox;
import com.tmap.mit.map_viewer.dto.Point;
import com.tmap.mit.map_viewer.dto.PolyTypeData;
import com.tmap.mit.map_viewer.dto.ShpData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MapDataService {
    @Cacheable(cacheNames = "getMapDataByShapeFile", key = "'getMapDataByShapeFile:'+#fileName")
    public ShpData getMapDataByShapeFileWithCache(String fileName) throws IOException {
        return this.getMapDataByShapeFile(fileName);
    }

    public ShpData getMapDataByShapeFile(String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource(String.format(FileConstant.SHP_FILE_PATH_FORMAT, fileName));
        try (FileChannel channel = new FileInputStream(resource.getFile()).getChannel()) {
            MappedByteBuffer headerBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, ShpHeader.SIZE);
            headerBuffer.order(ByteOrder.LITTLE_ENDIAN);

            int shapeType = headerBuffer.getInt(ShpHeader.IDX_SHAPE_TYPE);
            BoundingBox bbox = new BoundingBox(
                    headerBuffer.getDouble(ShpHeader.IDX_MIN_X),
                    headerBuffer.getDouble(ShpHeader.IDX_MIN_Y),
                    headerBuffer.getDouble(ShpHeader.IDX_MAX_X),
                    headerBuffer.getDouble(ShpHeader.IDX_MAX_Y));

            List<Point> points = new ArrayList<>();
            List<BoundingBox> recordBboxs = new ArrayList<>();
            List<PolyTypeData> polyTypeDatas = new ArrayList<>();

            boolean isPolyType = ShapeType.POLY.contains(shapeType);
            long position = ShpHeader.SIZE;

            while (position < channel.size()) {
                MappedByteBuffer recordBuffer = channel.map(FileChannel.MapMode.READ_ONLY, position, ShpRecordHeader.SIZE);
                recordBuffer.order(ByteOrder.BIG_ENDIAN);

                if (recordBuffer.remaining() < ShpRecordHeader.SIZE) break;
                int contentLength = recordBuffer.getInt(ShpRecordHeader.LENGTH) * 2;

                MappedByteBuffer contentBuffer = channel.map(FileChannel.MapMode.READ_ONLY, position + ShpRecordHeader.SIZE, contentLength);
                contentBuffer.order(ByteOrder.LITTLE_ENDIAN);

                if (ShapeType.POINT.getCode().equals(shapeType)) {
                    points.add(new Point(
                            contentBuffer.getDouble(ShpRecordContent.IDX_POINT_TYPE_X),
                            contentBuffer.getDouble(ShpRecordContent.IDX_POINT_TYPE_Y)));
                }

                if (isPolyType) {
                    recordBboxs.add(new BoundingBox(
                            contentBuffer.getDouble(ShpRecordContent.IDX_POLY_MIN_X),
                            contentBuffer.getDouble(ShpRecordContent.IDX_POLY_MIN_Y),
                            contentBuffer.getDouble(ShpRecordContent.IDX_POLY_MAX_X),
                            contentBuffer.getDouble(ShpRecordContent.IDX_POLY_MAX_Y)));

                    int numParts = contentBuffer.getInt(ShpRecordContent.IDX_NUM_PARTS);
                    int numPoints = contentBuffer.getInt(ShpRecordContent.IDX_NUM_POINTS);

                    int[] parts = new int[numParts];
                    int idxPartsStart = ShpRecordContent.IDX_PARTS;
                    for (int i = 0; i < numParts; i++) {
                        parts[i] = contentBuffer.getInt(idxPartsStart);
                        idxPartsStart += Integer.BYTES;
                    }

                    List<Point> polyPoints = new ArrayList<>();
                    int idxPolyX = ShpRecordContent.IDX_POLY_X;
                    int idxPolyY = ShpRecordContent.IDX_POLY_Y;
                    for (int i = 0; i < numPoints; i++) {
                        double x = contentBuffer.getDouble(idxPolyX);
                        double y = contentBuffer.getDouble(idxPolyY);
                        idxPolyX += Double.BYTES * 2;
                        idxPolyY += Double.BYTES * 2;
                        polyPoints.add(new Point(x, y));
                    }
                    polyTypeDatas.add(new PolyTypeData(polyPoints, parts));
                }
                position += ShpRecordHeader.SIZE + contentLength;
            }

            return isPolyType ? new ShpData(shapeType, bbox, polyTypeDatas, recordBboxs) : new ShpData(shapeType, bbox, points);
        }
    }
}