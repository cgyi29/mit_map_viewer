package com.tmap.mit.map_viewer.service.impl;

import com.tmap.mit.map_viewer.cd.FieldType;
import com.tmap.mit.map_viewer.cd.ShapeType;
import com.tmap.mit.map_viewer.constant.DbfFile;
import com.tmap.mit.map_viewer.constant.ShpFile;
import com.tmap.mit.map_viewer.dto.DbfDto;
import com.tmap.mit.map_viewer.dto.ShpDto;
import com.tmap.mit.map_viewer.service.DbfParserService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DbfParserServiceImpl implements DbfParserService {
    @Cacheable(cacheNames = "getDbfParserData", key = "'getDbfParserData:'+#fileName")
    public DbfDto.ResData getDbfParserDataWithCache(String fileName) throws IOException {
        return this.getDbfParserDataNoCache(fileName);
    }

    public DbfDto.ResData getDbfParserDataNoCache(String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource(String.format(DbfFile.DBF_FILE_PATH_FORMAT, fileName));
        try (FileChannel channel = new FileInputStream(resource.getFile()).getChannel()) {
            MappedByteBuffer headerBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            headerBuffer.order(ByteOrder.LITTLE_ENDIAN);

            int version = headerBuffer.get() ;
            int updateYear = DbfFile.UPDATE_YEAR_MINIMUM + headerBuffer.get(1);
            int numberOfRecords = headerBuffer.getInt(DbfFile.IDX_NUMBER_OF_RECORD);
            int headerLength = headerBuffer.getShort(DbfFile.IDX_HEADER_LENGTH);
            int recordLength = headerBuffer.getShort(DbfFile.IDX_RECORD_LENGTH);

            headerBuffer.position(DbfFile.IDX_HEADER_SIZE);
            List<DbfDto.FieldMetaData> fields = new ArrayList<>();
            while (headerBuffer.position() < headerLength -1) {
                byte[] fieldNameBytes = new byte[DbfFile.FIELD_NAME_BYTE];
                headerBuffer.get(fieldNameBytes, 0, 11);
                String fieldName = new String(fieldNameBytes, StandardCharsets.US_ASCII).trim();
                char fieldType = (char)(headerBuffer.get()&0xFF);
                headerBuffer.position(headerBuffer.position() +4);
                int fieldLength = headerBuffer.get()&0xFF;
                headerBuffer.position(headerBuffer.position() +15);

                fields.add(new DbfDto.FieldMetaData(fieldName, fieldType, fieldLength));
            }

            List<Map<String, Object>> records = new ArrayList<>();
            long recordStartPosition = headerLength;
            for(int recordIndex = 0; recordIndex < numberOfRecords; recordIndex++){
                MappedByteBuffer recordBuffer = channel.map(FileChannel.MapMode.READ_ONLY, recordStartPosition, recordLength);
                recordBuffer.order(ByteOrder.LITTLE_ENDIAN);

                Map<String, Object> record = new HashMap<>();
                for(DbfDto.FieldMetaData field : fields){
                    byte[] data = new byte[field.getLength()];
                    recordBuffer.get(data);
                    record.put(field.getName(), parseData(data, field.getType()));
                }
                records.add(record);
                recordStartPosition += recordLength;
            }
            return new DbfDto.ResData(fields, records);
        }
    }

    private static Object parseData(byte[] data, char type){
        String dataString = new String(data, StandardCharsets.UTF_8).trim();
        dataString = StringUtils.isBlank(dataString)?null:dataString;

        return FieldType.convertDataStringByType(type, dataString);
    }
}