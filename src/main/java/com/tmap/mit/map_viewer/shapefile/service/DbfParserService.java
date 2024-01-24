package com.tmap.mit.map_viewer.shapefile.service;

import com.tmap.mit.map_viewer.shapefile.constant.DbfFile;
import com.tmap.mit.map_viewer.shapefile.dto.DbfDto;
import com.tmap.mit.map_viewer.utils.FileReadUtils;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
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

/**
 * dBase file
 */
@Slf4j
@Service
public class DbfParserService {
    @Cacheable(cacheNames = "getDbfParserData", key = "'getDbfParserData:'+#path+':'+#fileName")
    public DbfDto.ResData getDbfParserDataWithCache(String path, String fileName) throws IOException {
        return this.getDbfParserDataNoCache(path, fileName);
    }

    public DbfDto.ResData getDbfParserDataNoCache(String path, String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource(String.format(DbfFile.DBF_FILE_PATH_FORMAT_NEW, path, fileName));
        try (FileChannel channel = new FileInputStream(resource.getFile()).getChannel()) {
            MappedByteBuffer headerBuffer = (MappedByteBuffer) channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size()).order(ByteOrder.LITTLE_ENDIAN);
            DbfDto.HeaderInfo headerInfo = parseHeader(headerBuffer);
            List<Map<String, Object>> records = parseRecords(headerBuffer, headerInfo);

            return new DbfDto.ResData(headerInfo.getFields(), records);
        }
    }

    private DbfDto.HeaderInfo parseHeader(MappedByteBuffer buffer) {
        int version = buffer.get();
        int updateYear = DbfFile.UPDATE_YEAR_MINIMUM + buffer.get(1);
        int numberOfRecords = buffer.getInt(DbfFile.IDX_NUMBER_OF_RECORD);
        int headerLength = buffer.getShort(DbfFile.IDX_HEADER_LENGTH);
        int recordLength = buffer.getShort(DbfFile.IDX_RECORD_LENGTH);
        List<DbfDto.FieldMetaData> fields = extractFields(buffer, headerLength);

        return new DbfDto.HeaderInfo(fields, numberOfRecords, headerLength, recordLength);
    }

    private List<DbfDto.FieldMetaData> extractFields(MappedByteBuffer buffer, int headerLength) {
        List<DbfDto.FieldMetaData> fields = new ArrayList<>();
        buffer.position(DbfFile.IDX_HEADER_SIZE);
        while (buffer.position() < headerLength - 1) {
            byte[] fieldNameBytes = new byte[DbfFile.FIELD_NAME_BYTE];
            buffer.get(fieldNameBytes, 0, DbfFile.FIELD_NAME_BYTE);
            String fieldName = new String(fieldNameBytes, StandardCharsets.US_ASCII).trim();
            char fieldType = (char) (buffer.get() & 0xFF);
            buffer.position(buffer.position() + 4); // Skip field descriptor array
            int fieldLength = buffer.get() & 0xFF;
            int decimalCount = buffer.get() & 0xFF;
            fields.add(new DbfDto.FieldMetaData(fieldName, fieldType, fieldLength, decimalCount));
            buffer.position(buffer.position() + 14); // Skip reserved bytes
        }
        return fields;
    }

    private List<Map<String, Object>> parseRecords(MappedByteBuffer buffer, DbfDto.HeaderInfo headerInfo) {
        List<Map<String, Object>> records = new ArrayList<>();
        int recordStartPosition = headerInfo.getHeaderLength();

        for (int recordIndex = 0; recordIndex < headerInfo.getNumberOfRecords(); recordIndex++) {
            buffer.position(recordStartPosition);
            byte deleteIndicator = buffer.get();

            if (deleteIndicator == (byte)0x2A) {
                recordStartPosition += headerInfo.getRecordLength();
                continue;
            }

            Map<String, Object> record = new HashMap<>();
            for (DbfDto.FieldMetaData field : headerInfo.getFields()) {
                byte[] data = new byte[field.getLength()];
                buffer.get(data);
                Object value = parseData(data, field.getType(), field.getDecimalCount());
                record.put(field.getName(), value);
            }
            records.add(record);
            recordStartPosition += headerInfo.getRecordLength();
        }
        return records;
    }

    private Object parseData(byte[] data, char type, int decimalCount){
        String dataString = new String(data, StandardCharsets.ISO_8859_1).trim();
        if (StringUtils.isBlank(dataString))  return null;

        return switch (type) {
            case 'D' -> parseDate(dataString);
            case 'N' -> parseNumeric(dataString, decimalCount);
            case 'L' -> parseBoolean(dataString);
            default -> dataString;
        };
    }

    private static LocalDate parseDate(String dataString) {
        if (dataString.length() == 8) {
            int year = Integer.parseInt(dataString.substring(0, 4));
            int month = Integer.parseInt(dataString.substring(4, 6));
            int day = Integer.parseInt(dataString.substring(6, 8));
            return LocalDate.of(year, month, day);
        }
        return null;
    }

    private static Object parseNumeric(String dataString, int decimalCount) {
        return (decimalCount > 0) ? Double.parseDouble(dataString) : Integer.parseInt(dataString);
    }

    private static Boolean parseBoolean(String dataString) {
        char ch = dataString.toUpperCase().charAt(0);
        return ch == 'T' || ch == 'Y';
    }
}