package com.tmap.mit.map_viewer.dto;

import lombok.Getter;
import java.util.List;

public class ShpDto {
    @Getter
    public static class ResData {
        private int shapeType;
        private BoundingBox bbox;
        private List<Point> points;
        private List<PolyTypeData> polyTypeDatas;
        private List<BoundingBox> recordBboxs;
        public ResData(int shapeType, BoundingBox bbox, List<Point> points) {
            this.shapeType = shapeType;
            this.bbox = bbox;
            this.points = points;
        }
        public ResData(int shapeType, BoundingBox bbox, List<PolyTypeData> polyTypeDatas, List<BoundingBox> recordBboxs) {
            this.shapeType = shapeType;
            this.bbox = bbox;
            this.polyTypeDatas = polyTypeDatas;
            this.recordBboxs = recordBboxs;
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
    public static class Point {
        private double x;
        private double y;
        public Point(double x, double y){
            this.x = x;
            this.y = y;
        }
    }

    @Getter
    public static class PolyTypeData {
        private List<Point> points;
        private int[] parts;
        public PolyTypeData(List<Point> points, int[] parts){
            this.points = points;
            this.parts = parts;
        }
    }
}