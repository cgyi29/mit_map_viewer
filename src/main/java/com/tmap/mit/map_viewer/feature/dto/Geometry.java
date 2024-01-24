package com.tmap.mit.map_viewer.feature.dto;

import com.tmap.mit.map_viewer.shapefile.dto.ShpDto;
import lombok.Getter;

import java.util.List;

@Getter
public class Geometry {
    private final String type;
    private final Object coordinates;

    public Geometry(String type, Object coordinates) {
        this.type = type;
        this.coordinates = coordinates;
    }

}
