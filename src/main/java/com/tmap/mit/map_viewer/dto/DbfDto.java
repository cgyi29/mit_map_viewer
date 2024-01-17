package com.tmap.mit.map_viewer.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DbfDto {
    @Getter
    public static class ResData {
        List<DbfDto.FieldMetaData> fields;
        List<Map<String, Object>> records;

        public ResData(List<DbfDto.FieldMetaData> fields, List<Map<String, Object>> records){
            this.fields = fields;
            this.records = records;
        }
    }

    @Getter
    public static class HeaderInfo {
        private final List<DbfDto.FieldMetaData> fields;
        private final int numberOfRecords;
        private final int headerLength;
        private final int recordLength;

        public HeaderInfo(List<DbfDto.FieldMetaData> fields, int numberOfRecords, int headerLength, int recordLength) {
            this.fields = fields;
            this.numberOfRecords = numberOfRecords;
            this.headerLength = headerLength;
            this.recordLength = recordLength;
        }
    }

    @Getter
    public static class FieldMetaData {
        private String name;
        private char type;
        private int length;
        private int decimalCount;

        public FieldMetaData(String name, char type, int length, int decimalCount){
            this.name = name;
            this.type = type;
            this.length = length;
            this.decimalCount =  decimalCount;
        }
    }
}
