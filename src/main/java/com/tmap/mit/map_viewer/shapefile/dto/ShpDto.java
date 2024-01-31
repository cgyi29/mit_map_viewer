package com.tmap.mit.map_viewer.shapefile.dto;

import com.tmap.mit.map_viewer.shapefile.cd.ShapeType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class ShpDto {
    @Getter
    public static class ResData {
        private final String type;
        private final BoundingBox bbox;
        private final List<CoordinateInfo> coordinateInfo;


        public ResData(String type, BoundingBox bbox, List<CoordinateInfo> coordinateInfo) {
            this.type = type;
            this.bbox = bbox;
            this.coordinateInfo = coordinateInfo;
        }
    }

    @Getter
    public static class BoundingBox {
        private double minX;
        private double minY;
        private double maxX;
        private double maxY;
        public BoundingBox(double minX, double minY, double maxX, double maxY){
            this.minX = minX;
            this.minY = minY;
            this.maxX = maxX;
            this.maxY = maxY;
        }
    }

    @Getter
    public static class Coordinates {
        private double x;
        private double y;
        public Coordinates(double x, double y){
            this.x = x;
            this.y = y;
        }
    }

    @Getter
    @Setter
    public static class CoordinateInfo {
        private List<Coordinates> coordinates;
        private int[] parts;
        private List<ShpDto.BoundingBox> recordBboxs;

        public CoordinateInfo(List<Coordinates> coordinates, int[] parts, List<ShpDto.BoundingBox> recordBboxs){
            this.coordinates = coordinates;
            this.parts = parts;
            //this.recordBboxs = recordBboxs;
        }
    }
}