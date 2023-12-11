package com.tmap.mit.map_viewer.service;

import com.tmap.mit.map_viewer.dto.Point;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MapDataService {
    private final String SHP_FILE_PATH_FORMAT = "files/%s.shp";

    public List<Point> createByShapeFile(String fileName) throws IOException {
        List<Point> points = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource(String.format(SHP_FILE_PATH_FORMAT, fileName));
        try (InputStream is = resource.getInputStream();
             BufferedInputStream bis = new BufferedInputStream(is)) {

            byte[] header = new byte[100];
            bis.read(header);
            ByteBuffer buffer = ByteBuffer.wrap(header);
            buffer.order(ByteOrder.BIG_ENDIAN);

            buffer.order(ByteOrder.LITTLE_ENDIAN);
            while (bis.available() > 0) {
                byte[] recordHeader = new byte[8];
                bis.read(recordHeader);
                ByteBuffer recordBuffer = ByteBuffer.wrap(recordHeader);
                recordBuffer.order(ByteOrder.LITTLE_ENDIAN);

                byte[] pointData = new byte[16];

                bis.read(pointData);
                ByteBuffer pointBuffer = ByteBuffer.wrap(pointData).order(ByteOrder.LITTLE_ENDIAN);

                double x = pointBuffer.getDouble();
                double y = pointBuffer.getDouble();

                points.add(Point.builder().x(x).y(y).build());
            }
        }
        return points;
    }

}
