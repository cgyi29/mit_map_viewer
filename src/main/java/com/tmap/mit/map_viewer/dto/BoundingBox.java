package com.tmap.mit.map_viewer.dto;

import lombok.Getter;

@Getter
public class BoundingBox {
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
