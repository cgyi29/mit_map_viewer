package com.tmap.mit.map_viewer.test;

import com.tmap.mit.map_viewer.dto.Point;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ArcShpParser {

    public static void main(String[] args) throws IOException {
        List<Point> points = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource("files/Duraklar.shp");
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

        points.stream().count();
    }
}
