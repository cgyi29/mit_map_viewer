package com.tmap.mit.map_viewer.test;

import com.tmap.mit.map_viewer.constant.FileConstant;
import com.tmap.mit.map_viewer.constant.ShxHeader;
import com.tmap.mit.map_viewer.constant.ShxRecord;
import com.tmap.mit.map_viewer.dto.ShxData;
import com.tmap.mit.map_viewer.dto.ShxRecordData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SHXParser {

    public static void main(String[] args) {
        ClassPathResource resource = new ClassPathResource(String.format(FileConstant.SHX_FILE_PATH_FORMAT, "Hatlar"));
        try (FileChannel channel = new FileInputStream(resource.getFile()).getChannel()) {
            MappedByteBuffer headerBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, ShxHeader.SIZE);
            headerBuffer.order(ByteOrder.LITTLE_ENDIAN);

            int fileLength = headerBuffer.getInt(ShxHeader.LENGTH);
            int version = headerBuffer.getInt(ShxHeader.VERSION);
            int shapeType = headerBuffer.getInt(ShxHeader.SHAPE_TYPE);

            List<ShxRecordData> records = new ArrayList<>();
            long position = ShxHeader.SIZE;
            while (position < fileLength * 2) {
                MappedByteBuffer indexBuffer = channel.map(FileChannel.MapMode.READ_ONLY, position, ShxRecord.SIZE);
                indexBuffer.order(ByteOrder.BIG_ENDIAN);

                int recordOffset = indexBuffer.getInt();
                int recordLength = indexBuffer.getInt();

                records.add(new ShxRecordData(recordOffset * 2, recordLength *2));
                position += ShxRecord.SIZE;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
