package com.tmap.mit.map_viewer.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ShapeData {
    private int shapeType;
    private BoundingBox bbox;
    private List<Coordinate> coordinates;

    public ShapeData(int shapeType, BoundingBox bbox, List<Coordinate> coordinates){
        this.shapeType = shapeType;
        this.bbox = bbox;
        this.coordinates = coordinates;
    }
}
