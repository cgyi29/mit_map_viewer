package com.tmap.mit.map_viewer.service;

import com.tmap.mit.map_viewer.cd.ShapeType;
import com.tmap.mit.map_viewer.constant.FileConstant;
import com.tmap.mit.map_viewer.constant.FileHeader;
import com.tmap.mit.map_viewer.constant.FileRecordContent;
import com.tmap.mit.map_viewer.constant.FileRecordHeader;
import com.tmap.mit.map_viewer.dto.BoundingBox;
import com.tmap.mit.map_viewer.dto.Point;
import com.tmap.mit.map_viewer.dto.PolyTypeData;
import com.tmap.mit.map_viewer.dto.ShapeData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MapDataService {
    public ShapeData getMapDataByShapeFile(String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource(String.format(FileConstant.SHP_FILE_PATH_FORMAT, fileName));
        try (FileInputStream fis = new FileInputStream(resource.getFile());
             FileChannel channel = fis.getChannel()) {
            ByteBuffer headerBuffer = ByteBuffer.allocate(FileHeader.SIZE);
            channel.read(headerBuffer);
            headerBuffer.flip();

            headerBuffer.order(ByteOrder.LITTLE_ENDIAN);
            int shapeType = headerBuffer.getInt(FileHeader.IDX_SHAPE_TYPE);

            BoundingBox bbox = new BoundingBox(
                    headerBuffer.getDouble(FileHeader.IDX_MIN_X),
                    headerBuffer.getDouble(FileHeader.IDX_MIN_Y),
                    headerBuffer.getDouble(FileHeader.IDX_MAX_X),
                    headerBuffer.getDouble(FileHeader.IDX_MAX_Y));

            ByteBuffer recordBuffer = ByteBuffer.allocate(FileRecordHeader.SIZE);
            recordBuffer.order(ByteOrder.BIG_ENDIAN);

            List<Point> points = new ArrayList<>();
            List<BoundingBox> recordBboxs = new ArrayList<>();
            List<PolyTypeData> polyTypeDatas = new ArrayList<>();

            boolean isPolyType = ShapeType.POLY.contains(shapeType);
            while(channel.read(recordBuffer) != -1){
                recordBuffer.flip();
                if(recordBuffer.remaining() < FileRecordHeader.SIZE)  break;
                int contentLength = recordBuffer.getInt(FileRecordHeader.LENGTH) * 2;

                ByteBuffer contentBuffer = ByteBuffer.allocate(contentLength);
                contentBuffer.order(ByteOrder.LITTLE_ENDIAN);

                channel.read(contentBuffer);
                contentBuffer.flip();

                if(ShapeType.POINT.getCode().equals(shapeType)) {
                    points.add(new Point(
                            contentBuffer.getDouble(FileRecordContent.IDX_POINT_TYPE_X),
                            contentBuffer.getDouble(FileRecordContent.IDX_POINT_TYPE_Y)));
                }

                if(isPolyType) {
                    recordBboxs.add(new BoundingBox(
                            contentBuffer.getDouble(FileRecordContent.IDX_POLY_MIN_X),
                            contentBuffer.getDouble(FileRecordContent.IDX_POLY_MIN_Y),
                            contentBuffer.getDouble(FileRecordContent.IDX_POLY_MAX_X),
                            contentBuffer.getDouble(FileRecordContent.IDX_POLY_MAX_Y)));

                    int numParts = contentBuffer.getInt(FileRecordContent.IDX_NUM_PARTS);
                    int numPoints = contentBuffer.getInt(FileRecordContent.IDX_NUM_POINTS);

                    int[] parts = new int[numParts];
                    int idxPartsStart = FileRecordContent.IDX_PARTS;
                    for (int i=0; i<numParts; i++) {
                        parts[i] = contentBuffer.getInt(idxPartsStart);
                        idxPartsStart += Integer.BYTES;
                    }

                    List<Point> polyPoints = new ArrayList<>();
                    int idxPolyX = FileRecordContent.IDX_POLY_X;
                    int idxPolyY = FileRecordContent.IDX_POLY_Y;
                    for(int i=0; i<numPoints; i++){
                        double x = contentBuffer.getDouble(idxPolyX);
                        double y = contentBuffer.getDouble(idxPolyY);
                        idxPolyX += Double.BYTES * 2;
                        idxPolyY += Double.BYTES * 2;
                        polyPoints.add(new Point(x, y));
                    }
                    polyTypeDatas.add(new PolyTypeData(polyPoints, parts));
                }
                recordBuffer.clear();
            }
            return isPolyType ? new ShapeData(shapeType, bbox, polyTypeDatas, recordBboxs) : new ShapeData(shapeType, bbox, points);
        }


    }

}
