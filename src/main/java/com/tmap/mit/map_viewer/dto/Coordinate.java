package com.tmap.mit.map_viewer.dto;

import lombok.Getter;

@Getter
public class Coordinate {
    private double x;
    private double y;

    public Coordinate(double x, double y){
        this.x = x;
        this.y = y;
    }
}
