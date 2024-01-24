package com.tmap.mit.map_viewer.shapefile.dto;

import com.tmap.mit.map_viewer.shapefile.cd.ShapeType;
import lombok.Getter;

import java.util.List;

public class ShpDto {
    @Getter
    public static class ResData {
        private ShapeType type;
        private BoundingBox bbox;
        private List<Object> coordinates;

        public ResData(ShapeType type, BoundingBox bbox, List<Object> coordinates) {
            this.type = type;
            this.bbox = bbox;
            this.coordinates = coordinates;
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
}