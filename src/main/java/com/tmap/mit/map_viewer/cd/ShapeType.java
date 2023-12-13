package com.tmap.mit.map_viewer.cd;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * shape file type enum
 */
@Getter
@AllArgsConstructor
public enum ShapeType {
    POINT(1, "point shape type"),
    POLYLINE(3, "polyLine shape type"),
    POLYGON(5, "polygon shape type");

    public static final List<Integer> POLY;
    static {
        POLY = new ArrayList<>();
        POLY.add(POLYLINE.code);
        POLY.add(POLYGON.code);
    }

    private final Integer code;
    private final String desc;
}
