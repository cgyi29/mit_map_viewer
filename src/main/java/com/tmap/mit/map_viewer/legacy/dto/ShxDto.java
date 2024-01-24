package com.tmap.mit.map_viewer.legacy.dto;

import lombok.Getter;

import java.util.List;

public class ShxDto {
    @Getter
    public static class ResData {
        private int shapeType;
        List<RecordData> records;
        public ResData(int shapeType, List<RecordData> records){
            this.shapeType = shapeType;
            this.records = records;
        }
    }

    @Getter
    public static class RecordData {
        int offset;
        int length;
        public RecordData(int offset, int length){
            this.offset = offset;
            this.length = length;
        }
    }
}
