package com.tmap.mit.map_viewer.shapefile.dto;

import com.tmap.mit.map_viewer.shapefile.cd.ShapeType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

public class ShpDto {
    private final static double ratioPointCut = 100000;

    @Getter
    public static class ResData {
        private final String type;
        private final BoundingBox bbox;
        private final List<CoordinateInfo> coordinateInfo;
        private final Map<Integer, List<ShpDto.CoordinateInfo>> coordinateInfoMap;


        public ResData(String type, BoundingBox bbox, List<CoordinateInfo> coordinateInfo, Map<Integer, List<ShpDto.CoordinateInfo>> coordinateInfoMap) {
            this.type = type;
            this.bbox = bbox;
            this.coordinateInfo = coordinateInfo;
            this.coordinateInfoMap = coordinateInfoMap;
        }
    }

    @ToString
    @Getter
    public static class BoundingBox {
        private double minX;
        private double minY;
        private double maxX;
        private double maxY;
        public BoundingBox(double minX, double minY, double maxX, double maxY){
            this.minX = mathFloor(minX);
            this.minY = mathFloor(minY);
            this.maxX = mathFloor(maxX);
            this.maxY = mathFloor(maxY);
        }
    }

    @Getter
    public static class Coordinates {
        private double x;
        private double y;
        public Coordinates(double x, double y){
            this.x = mathFloor(x);
            this.y = mathFloor(y);
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

    public static double mathFloor(double coordinate){
        return Math.floor(coordinate* ratioPointCut)/ratioPointCut;
    }
}