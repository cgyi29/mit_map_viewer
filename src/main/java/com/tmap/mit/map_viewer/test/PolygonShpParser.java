package com.tmap.mit.map_viewer.test;

import com.tmap.mit.map_viewer.constant.FileConstant;
import com.tmap.mit.map_viewer.constant.FileHeader;
import com.tmap.mit.map_viewer.constant.FileRecordHeader;
import com.tmap.mit.map_viewer.dto.BoundingBox;
import com.tmap.mit.map_viewer.dto.Coordinate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PolygonShpParser {

    public static void main(String[] args) {
        ClassPathResource resource = new ClassPathResource("files/Hatlar.shp");
        //ClassPathResource resource = new ClassPathResource(String.format(FileConstant.SHP_FILE_PATH_FORMAT, fileName));
        try (FileInputStream fis = new FileInputStream(resource.getFile());
             FileChannel channel = fis.getChannel()) {
            ByteBuffer headerBuffer = ByteBuffer.allocate(FileHeader.SIZE);
            channel.read(headerBuffer);
            headerBuffer.flip();

            int fileCode = headerBuffer.getInt();
            int fileLength = headerBuffer.getInt(FileHeader.LENGTH);

            headerBuffer.order(ByteOrder.LITTLE_ENDIAN);
            int shapeType = headerBuffer.getInt(FileHeader.IDX_SHAPE_TYPE);

            BoundingBox bbox = new BoundingBox(
                    headerBuffer.getDouble(FileHeader.IDX_MIN_X), headerBuffer.getDouble(FileHeader.IDX_MIN_Y),
                    headerBuffer.getDouble(FileHeader.IDX_MAX_X),headerBuffer.getDouble(FileHeader.IDX_MAX_Y));

            ByteBuffer recordBuffer = ByteBuffer.allocate(FileRecordHeader.SIZE);
            recordBuffer.order(ByteOrder.BIG_ENDIAN);

            List<BoundingBox> recordBboxs = new ArrayList<>();
            List<Coordinate> coordinates = new ArrayList<>();
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
                double contentMinX = contentBuffer.getDouble(4);
                double contentMinY = contentBuffer.getDouble(12);
                double contentMaxX = contentBuffer.getDouble(20);
                double contentMaxY = contentBuffer.getDouble(28);
                recordBboxs.add(new BoundingBox(contentMinX, contentMinY, contentMaxX, contentMaxY));

                // parts and Points
                int numParts = contentBuffer.getInt(36);
                int numPoints = contentBuffer.getInt(40);

                int[] parts = new int[numParts];
                for(int i=0; i<numParts; i++){
                    parts[i] = contentBuffer.getInt();
                }

                for(int i=0; i<numPoints; i++){
                    double pointX = contentBuffer.getDouble();
                    double pointY = contentBuffer.getDouble();
                }
                recordBuffer.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
