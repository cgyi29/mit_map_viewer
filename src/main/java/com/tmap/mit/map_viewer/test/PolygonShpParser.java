package com.tmap.mit.map_viewer.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

@Slf4j
public class PolygonShpParser {

    public static void main(String[] args) {
        ClassPathResource resource = new ClassPathResource("files/Hatlar.shp");
        try (FileInputStream fis = new FileInputStream(resource.getFile());
             FileChannel channel = fis.getChannel()) {

            // shapefile header read
            ByteBuffer buffer = ByteBuffer.allocate(100);
            buffer.order(ByteOrder.BIG_ENDIAN);

            channel.read(buffer);
            buffer.flip();
            buffer.position(100);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
