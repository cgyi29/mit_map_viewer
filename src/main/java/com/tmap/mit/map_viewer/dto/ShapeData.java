package com.tmap.mit.map_viewer.dto;

import lombok.Getter;
import java.util.List;

@Getter
public class ShapeData {
    private int shapeType;
    private BoundingBox bbox;
    private List<Coordinate> pointCoordinates;

    private List<BoundingBox> recordBboxs;
    private List<Double[]> polyCoordinates;

    public ShapeData(int shapeType, BoundingBox bbox, List<Coordinate> pointCoordinates){
        this.shapeType = shapeType;
        this.bbox = bbox;
        this.pointCoordinates = pointCoordinates;
    }

    public ShapeData(int shapeType, BoundingBox bbox, List<BoundingBox> recordBboxs, List<Double[]> polyCoordinates){
        this.shapeType = shapeType;
        this.bbox = bbox;
        this.recordBboxs = recordBboxs;
        this.polyCoordinates = polyCoordinates;
    }
}
