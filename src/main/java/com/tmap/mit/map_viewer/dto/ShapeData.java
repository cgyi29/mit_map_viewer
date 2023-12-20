package com.tmap.mit.map_viewer.dto;

import lombok.Getter;
import java.util.List;

@Getter
public class ShapeData {
    private int shapeType;
    private BoundingBox bbox;
    private List<Point> points;
    private List<PolyTypeData> polyTypeDatas;
    private List<BoundingBox> recordBboxs;


    public ShapeData(int shapeType, BoundingBox bbox, List<Point> points){
        this.shapeType = shapeType;
        this.bbox = bbox;
        this.points = points;
    }

    public ShapeData(int shapeType, BoundingBox bbox, List<PolyTypeData> polyTypeDatas, List<BoundingBox> recordBboxs){
        this.shapeType = shapeType;
        this.bbox = bbox;
        this.polyTypeDatas = polyTypeDatas;
        this.recordBboxs = recordBboxs;
    }
}
