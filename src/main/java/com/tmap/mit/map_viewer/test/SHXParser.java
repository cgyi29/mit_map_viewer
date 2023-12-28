package com.tmap.mit.map_viewer.test;

import com.tmap.mit.map_viewer.constant.ShxFile;
import com.tmap.mit.map_viewer.dto.ShxDto;
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
        ClassPathResource resource = new ClassPathResource(String.format(ShxFile.SHX_FILE_PATH_FORMAT, "Hatlar"));
        try (FileChannel channel = new FileInputStream(resource.getFile()).getChannel()) {
            MappedByteBuffer headerBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, ShxFile.HEADER_SIZE);
            headerBuffer.order(ByteOrder.LITTLE_ENDIAN);

            int fileLength = headerBuffer.getInt(ShxFile.HEADER_LENGTH);
            int version = headerBuffer.getInt(ShxFile.IDX_HEADER_VERSION);
            int shapeType = headerBuffer.getInt(ShxFile.IDX_HEADER_SHAPE_TYPE);

            List<ShxDto.RecordData> records = new ArrayList<>();
            long position = ShxFile.HEADER_SIZE;
            while (position < fileLength * 2) {
                MappedByteBuffer indexBuffer = channel.map(FileChannel.MapMode.READ_ONLY, position, ShxFile.RECORD_SIZE);
                indexBuffer.order(ByteOrder.BIG_ENDIAN);

                int recordOffset = indexBuffer.getInt();
                int recordLength = indexBuffer.getInt();

                records.add(new ShxDto.RecordData(recordOffset * 2, recordLength *2));
                position += ShxFile.RECORD_SIZE;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
