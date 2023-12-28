package com.tmap.mit.map_viewer.service.impl;

import com.tmap.mit.map_viewer.cd.ShapeType;
import com.tmap.mit.map_viewer.constant.ShpFile;
import com.tmap.mit.map_viewer.dto.ShpDto;
import com.tmap.mit.map_viewer.service.DbfService;
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
public class DbfParserServiceImpl implements DbfService {
    @Cacheable(cacheNames = "getDbfParserData", key = "'getDbfParserData:'+#fileName")
    public ShpDto.ResData getDbfParserDataWithCache(String fileName) throws IOException {
        return this.getDbfParserDataNoCache(fileName);
    }

    public ShpDto.ResData getDbfParserDataNoCache(String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource(String.format(ShpFile.SHP_FILE_PATH_FORMAT, fileName));
        try (FileChannel channel = new FileInputStream(resource.getFile()).getChannel()) {
            MappedByteBuffer headerBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, ShpFile.HEADER_SIZE);
            headerBuffer.order(ByteOrder.LITTLE_ENDIAN);

            int shapeType = headerBuffer.getInt(ShpFile.IDX_HEADER_SHAPE_TYPE);
            ShpDto.BoundingBox bbox = new ShpDto.BoundingBox(
                    headerBuffer.getDouble(ShpFile.IDX_HEADER_MIN_X), headerBuffer.getDouble(ShpFile.IDX_HEADER_MIN_Y),
                    headerBuffer.getDouble(ShpFile.IDX_HEADER_MAX_X), headerBuffer.getDouble(ShpFile.IDX_HEADER_MAX_Y));

            List<ShpDto.Point> points = new ArrayList<>();
            List<ShpDto.BoundingBox> recordBboxs = new ArrayList<>();
            List<ShpDto.PolyTypeData> polyTypeDatas = new ArrayList<>();

            boolean isPolyType = ShapeType.POLY.contains(shapeType);
            long position = ShpFile.HEADER_SIZE;

            while (position < channel.size()) {
                MappedByteBuffer recordBuffer = channel.map(FileChannel.MapMode.READ_ONLY, position, ShpFile.RECORD_HEADER_SIZE);
                recordBuffer.order(ByteOrder.BIG_ENDIAN);

                if (recordBuffer.remaining() < ShpFile.RECORD_HEADER_SIZE) break;
                int contentLength = recordBuffer.getInt(ShpFile.RECORD_HEADER_LENGTH) * 2;

                MappedByteBuffer contentBuffer = channel.map(FileChannel.MapMode.READ_ONLY, position + ShpFile.RECORD_HEADER_SIZE, contentLength);
                contentBuffer.order(ByteOrder.LITTLE_ENDIAN);

                if (ShapeType.POINT.getCode().equals(shapeType)) {
                    points.add(new ShpDto.Point(
                            contentBuffer.getDouble(ShpFile.IDX_RECORD_CONTENT_POINT_TYPE_X),
                            contentBuffer.getDouble(ShpFile.IDX_RECORD_CONTENT_POINT_TYPE_Y)));
                }

                if (isPolyType) {
                    recordBboxs.add(new ShpDto.BoundingBox(
                            contentBuffer.getDouble(ShpFile.IDX_RECORD_CONTENT_POLY_MIN_X),
                            contentBuffer.getDouble(ShpFile.IDX_RECORD_CONTENT_POLY_MIN_Y),
                            contentBuffer.getDouble(ShpFile.IDX_RECORD_CONTENT_POLY_MAX_X),
                            contentBuffer.getDouble(ShpFile.IDX_RECORD_CONTENT_POLY_MAX_Y)));

                    int numParts = contentBuffer.getInt(ShpFile.IDX_RECORD_CONTENT_NUM_PARTS);
                    int numPoints = contentBuffer.getInt(ShpFile.IDX_RECORD_CONTENT_NUM_POINTS);

                    int[] parts = new int[numParts];
                    int idxPartsStart = ShpFile.IDX_RECORD_CONTENT_PARTS;
                    for (int i = 0; i < numParts; i++) {
                        parts[i] = contentBuffer.getInt(idxPartsStart);
                        idxPartsStart += Integer.BYTES;
                    }

                    List<ShpDto.Point> polyPoints = new ArrayList<>();
                    int idxPolyX = ShpFile.IDX_RECORD_CONTENT_POLY_X;
                    int idxPolyY = ShpFile.IDX_RECORD_CONTENT_POLY_Y;
                    for (int i = 0; i < numPoints; i++) {
                        double x = contentBuffer.getDouble(idxPolyX);
                        double y = contentBuffer.getDouble(idxPolyY);
                        idxPolyX += Double.BYTES * 2;
                        idxPolyY += Double.BYTES * 2;
                        polyPoints.add(new ShpDto.Point(x, y));
                    }
                    polyTypeDatas.add(new ShpDto.PolyTypeData(polyPoints, parts));
                }
                position += ShpFile.RECORD_HEADER_SIZE + contentLength;
            }

            return isPolyType ? new ShpDto.ResData(shapeType, bbox, polyTypeDatas, recordBboxs) : new ShpDto.ResData(shapeType, bbox, points);
        }
    }
}