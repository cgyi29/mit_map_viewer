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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

            List<Coordinate> coordinates = new ArrayList<>();
            List<BoundingBox> recordBboxs = new ArrayList<>();
            List<Double[]> polyCoordinates = new ArrayList<>();

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
                    coordinates.add(new Coordinate(
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

                    int[] partsStartIndex = new int[numParts];
                    for(int i=0; i<numParts; i++){
                        partsStartIndex[i] = contentBuffer.getInt();
                    }

                    List<Double[]> points = new ArrayList<>();
                    for(int i=0; i<numPoints; i++){
                        double x = contentBuffer.getDouble(FileRecordContent.IDX_POLY_X);
                        double y = contentBuffer.getDouble(FileRecordContent.IDX_POLY_Y);
                        points.add(new Double[]{x, y});
                    }

                    for(int partIndex=0; partIndex<numParts; partIndex++){
                        int start = partsStartIndex[partIndex];
                        int end = (partIndex < numParts -1) ? partsStartIndex[partIndex + 1] : numPoints;
                        for(int i = start; i<end; i++){
                            polyCoordinates.add(points.get(i));
                        }
                    }
                }
                recordBuffer.clear();
            }
            return isPolyType ? new ShapeData(shapeType, bbox, recordBboxs, polyCoordinates) : new ShapeData(shapeType, bbox, coordinates);
        }


    }

}
