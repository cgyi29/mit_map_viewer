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
    public static class FieldMetaData {
        private String name;
        private char type;
        private int length;
        public FieldMetaData(String name, char type, int length){
            this.name = name;
            this.type = type;
            this.length = length;
        }
    }
}
