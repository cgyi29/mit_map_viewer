package com.tmap.mit.map_viewer.test;

import com.tmap.mit.map_viewer.constant.FileConstant;
import com.tmap.mit.map_viewer.dto.FieldMetaData;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
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
public class DBFParser {

    public static void main(String[] args) {
        ClassPathResource resource = new ClassPathResource(String.format(FileConstant.DBF_FILE_PATH_FORMAT, "Hatlar"));
        try (FileChannel channel = new FileInputStream(resource.getFile()).getChannel()) {
            MappedByteBuffer headerBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            headerBuffer.order(ByteOrder.LITTLE_ENDIAN);

            int version = headerBuffer.get() ;
            int updateYear = 1900 + headerBuffer.get(1);
            int numberOfRecords = headerBuffer.getInt(4);
            int headerLength = headerBuffer.getShort(8);
            int recordLength = headerBuffer.getShort(10);

            headerBuffer.position(32);
            List<FieldMetaData> fields = new ArrayList<>();
            while (headerBuffer.position() < headerLength -1) {
                byte[] fieldNameBytes = new byte[11];
                headerBuffer.get(fieldNameBytes, 0, 11);
                String fieldName = new String(fieldNameBytes, StandardCharsets.US_ASCII).trim();
                char fieldType = (char)(headerBuffer.get()&0xFF);
                headerBuffer.position(headerBuffer.position() +4);
                int fieldLength = headerBuffer.get()&0xFF;
                headerBuffer.position(headerBuffer.position() +15);

                fields.add(new FieldMetaData(fieldName, fieldType, fieldLength));
            }

            List<Map<String, Object>> records = new ArrayList<>();
            long recordStartPosition = headerLength;
            for(int recordIndex = 0; recordIndex < numberOfRecords; recordIndex++){
                MappedByteBuffer recordBuffer = channel.map(FileChannel.MapMode.READ_ONLY, recordStartPosition, recordLength);
                recordBuffer.order(ByteOrder.LITTLE_ENDIAN);

                Map<String, Object> record = new HashMap<>();
                for(FieldMetaData field : fields){
                    byte[] data = new byte[field.getLength()];
                    recordBuffer.get(data);
                    record.put(field.getName(), parseData(data, field.getType()));
                }
                records.add(record);
                recordStartPosition += recordLength;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static Object parseData(byte[] data, char type){
        String dataString = new String(data, StandardCharsets.UTF_8).trim();
        dataString = StringUtils.isBlank(dataString)?"0":dataString;

        switch (type) {
            case 'C': // Character
                return dataString;
            case 'D': // Date
                // Assuming the format is YYYYMMDD
                if (dataString.length() == 8) {
                    int year = Integer.parseInt(dataString.substring(0, 4));
                    int month = Integer.parseInt(dataString.substring(4, 6));
                    int day = Integer.parseInt(dataString.substring(6, 8));
                    return LocalDate.of(year, month, day);
                }
                return null;
            case 'F': // Floating point
                return Float.parseFloat(dataString);
            case 'N': // Numeric
                // Can be either Integer or Double, depending on the presence of a decimal point
                if (dataString.contains(".")) {
                    return Double.parseDouble(dataString);
                } else {
                    return Integer.parseInt(dataString);
                }
            case 'L': // Logical
                // Typically T/F or Y/N
                char ch = dataString.toUpperCase().charAt(0);
                return ch == 'T' || ch == 'Y';
            case 'M': // Memo
                // Memo handling can vary depending on the DBF file structure
                // Return as string or handle accordingly
                return dataString;
            default:
                // For unrecognized types, return the raw string
                return dataString;
        }
    }
}
