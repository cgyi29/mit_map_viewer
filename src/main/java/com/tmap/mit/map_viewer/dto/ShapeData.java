package com.tmap.mit.map_viewer.dto;

import lombok.Getter;
import java.util.List;
import java.util.Map;

@Getter
public class ShapeData {
    private int shapeType;
    private BoundingBox bbox;
    private List<Coordinate> coordinates;

    private List<BoundingBox> recordBboxs;
    private Map<Integer, List<Coordinate>> coordinatesMap;

    public ShapeData(int shapeType, BoundingBox bbox, List<Coordinate> coordinates){
        this.shapeType = shapeType;
        this.bbox = bbox;
        this.coordinates = coordinates;
    }

    public ShapeData(int shapeType, BoundingBox bbox, List<BoundingBox> recordBboxs, Map<Integer, List<Coordinate>> coordinatesMap){
        this.shapeType = shapeType;
        this.bbox = bbox;
        this.recordBboxs = recordBboxs;
        this.coordinatesMap = coordinatesMap;
    }
}
