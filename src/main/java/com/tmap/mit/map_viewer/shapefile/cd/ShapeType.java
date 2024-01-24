package com.tmap.mit.map_viewer.shapefile.cd;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
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

    public static ShapeType getShapeTypeByCode(int code){
        return Arrays.stream(ShapeType.values())
                .filter(shapeType -> shapeType.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 shape type 입니다."));
    }
}
