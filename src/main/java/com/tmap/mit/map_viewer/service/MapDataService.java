package com.tmap.mit.map_viewer.service;

import com.tmap.mit.map_viewer.constant.FileHeader;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class MapDataService {
    private final String SHP_FILE_PATH_FORMAT = "files/%s.shp";

    public ShapeData getMapDataByShapeFile(String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource(String.format(SHP_FILE_PATH_FORMAT, fileName));
        try (FileInputStream fis = new FileInputStream(resource.getFile());
             FileChannel channel = fis.getChannel()) {

            ByteBuffer headerBuffer = ByteBuffer.allocate(FileHeader.HEADER_SIZE);
            headerBuffer.order(ByteOrder.BIG_ENDIAN);
            channel.read(headerBuffer);
            headerBuffer.flip();

            int fileCode = headerBuffer.getInt();
            headerBuffer.position(24);
            int fileLength = headerBuffer.getInt();

            headerBuffer.order(ByteOrder.LITTLE_ENDIAN);
            int version = headerBuffer.getInt();
            int shapeType = headerBuffer.getInt();

            double minX = headerBuffer.getDouble();
            double minY = headerBuffer.getDouble();
            double maxX = headerBuffer.getDouble();
            double maxY = headerBuffer.getDouble();
            BoundingBox bbox = new BoundingBox(minX, minY, maxX, maxY);

            List<Coordinate> coordinates = new ArrayList<>();
            ByteBuffer recordBuffer = ByteBuffer.allocate(8);
            recordBuffer.order(ByteOrder.BIG_ENDIAN);

            while(channel.read(recordBuffer) != -1){
                recordBuffer.flip();
                if(recordBuffer.remaining() < 8)  break;

                int recordNumber = recordBuffer.getInt();
                int contentLength = recordBuffer.getInt() * 2;

                ByteBuffer contentBuffer = ByteBuffer.allocate(contentLength);
                contentBuffer.order(ByteOrder.LITTLE_ENDIAN);

                channel.read(contentBuffer);
                contentBuffer.flip();

                int recordShapeType = contentBuffer.getInt();

                double x = contentBuffer.getDouble();
                double y = contentBuffer.getDouble();
                coordinates.add(new Coordinate(x, y));
                recordBuffer.clear();
            }

            return new ShapeData(shapeType, bbox, coordinates);
        }
    }


}
