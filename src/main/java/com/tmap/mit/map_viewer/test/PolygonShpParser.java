package com.tmap.mit.map_viewer.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

@Slf4j
public class ArcShpParser {

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

            while(channel.position() < channel.size()){
                buffer.clear();
                channel.read(buffer);
                buffer.flip();

                while (buffer.remaining() > 8){
                    buffer.getInt();
                    int contentLength = buffer.getInt() * 2;

                    buffer.order(ByteOrder.LITTLE_ENDIAN);


                    if(buffer.remaining() >=4){
                        int shapeType = buffer.getInt();

                        if(shapeType == 3) {
                            double xx = buffer.getDouble();
                            double yy = buffer.getDouble();
                            double xm = buffer.getDouble();
                            double ym = buffer.getDouble();

                            int numParts = buffer.getInt();
                            int numPoints = buffer.getInt();

                            for (int i = 0; i < numParts; i++) {
                                buffer.getInt();
                            }
                            for (int i = 0; i < numPoints; i++) {
                                double x = buffer.getDouble();
                                double y = buffer.getDouble();

                                log.info("Point x ::: {}, Point y ::: {}", x, y);
                            }
                        }
                    }

                    buffer.order(ByteOrder.BIG_ENDIAN);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
