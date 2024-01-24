package com.tmap.mit.map_viewer.shapefile.dto;

import lombok.Getter;

import java.util.List;

public class ShxDto {
    @Getter
    public static class ResData {
        private int id;
        private int shapeType;
        List<RecordData> records;
        public ResData(int id, List<RecordData> records){
            this.id = id;
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
