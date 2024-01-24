package com.tmap.mit.map_viewer.shapefile.service;

import com.tmap.mit.map_viewer.shapefile.constant.ShxFile;
import com.tmap.mit.map_viewer.shapefile.dto.ShxDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ShxParserService  {
    @Cacheable(cacheNames = "getShxParserData", key = "'getShxParserData:'+#fileName")
    public ShxDto.ResData getShxParserDataWithCache(String fileName) throws IOException {
        return this.getShxParserDataNoCache(fileName);
    }

    public ShxDto.ResData getShxParserDataNoCache(String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource(String.format(ShxFile.SHX_FILE_PATH_FORMAT, fileName));
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

                int recordOffset = indexBuffer.getInt(); // 시작 위치
                int recordLength = indexBuffer.getInt(); // 데이터 길이

                records.add(new ShxDto.RecordData(recordOffset * 2, recordLength *2));
                position += ShxFile.RECORD_SIZE;
            }

            return new ShxDto.ResData(shapeType, records);
        }
    }
}