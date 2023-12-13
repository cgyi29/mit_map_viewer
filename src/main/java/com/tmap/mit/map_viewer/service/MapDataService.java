package com.tmap.mit.map_viewer.service;

import com.tmap.mit.map_viewer.cd.ShapeType;
import com.tmap.mit.map_viewer.constant.FileConstant;
import com.tmap.mit.map_viewer.constant.FileHeader;
import com.tmap.mit.map_viewer.constant.FileRecordContent;
import com.tmap.mit.map_viewer.constant.FileRecordHeader;
import com.tmap.mit.map_viewer.dto.BoundingBox;
import com.tmap.mit.map_viewer.dto.Coordinate;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            //int fileCode = headerBuffer.getInt();
            //int fileLength = headerBuffer.getInt(FileHeader.LENGTH);
            headerBuffer.order(ByteOrder.LITTLE_ENDIAN);
            int shapeType = headerBuffer.getInt(FileHeader.IDX_SHAPE_TYPE);

            BoundingBox bbox = new BoundingBox(
                    headerBuffer.getDouble(FileHeader.IDX_MIN_X),
                    headerBuffer.getDouble(FileHeader.IDX_MIN_Y),
                    headerBuffer.getDouble(FileHeader.IDX_MAX_X),
                    headerBuffer.getDouble(FileHeader.IDX_MAX_Y));

            ByteBuffer recordBuffer = ByteBuffer.allocate(FileRecordHeader.SIZE);
            recordBuffer.order(ByteOrder.BIG_ENDIAN);

            List<Coordinate> coordinates = new ArrayList<>();
            List<BoundingBox> recordBboxs = new ArrayList<>();
            Map<Integer, List<Coordinate>> coordinatesMap = new HashMap<>();

            int initIdx = 1;
            boolean isPolyType = ShapeType.POLY.contains(shapeType);
            while(channel.read(recordBuffer) != -1){
                recordBuffer.flip();
                if(recordBuffer.remaining() < FileRecordHeader.SIZE)  break;
                //int recordNumber = recordBuffer.getInt();
                int contentLength = recordBuffer.getInt(FileRecordHeader.LENGTH) * 2;

                ByteBuffer contentBuffer = ByteBuffer.allocate(contentLength);
                contentBuffer.order(ByteOrder.LITTLE_ENDIAN);

                channel.read(contentBuffer);
                contentBuffer.flip();

                //int recordShapeType = contentBuffer.getInt(FileRecordContent.IDX_SHAPE_TYPE);
                if(ShapeType.POINT.getCode().equals(shapeType)) {
                    coordinates.add(new Coordinate(
                            contentBuffer.getDouble(FileRecordContent.IDX_POINT_TYPE_X),
                            contentBuffer.getDouble(FileRecordContent.IDX_POINT_TYPE_Y)));
                }

                if(isPolyType) {
                    recordBboxs.add(new BoundingBox(
                            contentBuffer.getDouble(FileRecordContent.IDX_POLYGON_MIN_X),
                            contentBuffer.getDouble(FileRecordContent.IDX_POLYGON_MIN_Y),
                            contentBuffer.getDouble(FileRecordContent.IDX_POLYGON_MAX_X),
                            contentBuffer.getDouble(FileRecordContent.IDX_POLYGON_MAX_Y)));

                    //int numParts = contentBuffer.getInt(FileRecordContent.IDX_NUM_PARTS);
                    int numPoints = contentBuffer.getInt(FileRecordContent.IDX_NUM_POINTS);

                    List<Coordinate> coordinateList = new ArrayList<>();
                    for(int i=0; i<numPoints; i++){
                        double pointX = contentBuffer.getDouble();
                        double pointY = contentBuffer.getDouble();
                        coordinateList.add(new Coordinate(pointX, pointY));
                    }
                    coordinatesMap.put(initIdx, coordinateList);
                    initIdx++;
                }
                recordBuffer.clear();
            }
            return isPolyType ? new ShapeData(shapeType, bbox, recordBboxs, coordinatesMap) : new ShapeData(shapeType, bbox, coordinates);
        }
    }

}
