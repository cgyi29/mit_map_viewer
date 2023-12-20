package com.tmap.mit.map_viewer.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PolyTypeData {
    private List<Point> points;
    private int[] parts;

    public PolyTypeData(List<Point> points, int[] parts){
        this.points = points;
        this.parts = parts;
    }

}
