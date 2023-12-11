package com.tmap.mit.map_viewer.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

@Slf4j
public class PointShpParser {

    public static void main(String[] args) {
        ClassPathResource resource = new ClassPathResource("files/Duraklar.shp");
        try (FileInputStream fis = new FileInputStream(resource.getFile());
             FileChannel channel = fis.getChannel()) {

            // shapefile header read
            ByteBuffer buffer = ByteBuffer.allocate(100);
            buffer.order(ByteOrder.BIG_ENDIAN);

            channel.read(buffer);
            buffer.flip();
            buffer.position(100);

            while(channel.position() < channel.size()){
                buffer.clear();
                channel.read(buffer);
                buffer.flip();

                while (buffer.remaining() > 8){
                    buffer.getInt();
                    int contentLength = buffer.getInt() * 2;

                    buffer.order(ByteOrder.LITTLE_ENDIAN);
                    int shapeType = buffer.getInt();

                    if(buffer.remaining() >= 20){
                        double x = buffer.getDouble();
                        double y = buffer.getDouble();

                        log.info("Point x ::: {}, Point y ::: {}", x, y);
                    }

                    buffer.order(ByteOrder.BIG_ENDIAN);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
