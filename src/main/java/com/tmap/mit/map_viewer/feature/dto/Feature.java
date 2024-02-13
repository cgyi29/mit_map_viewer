package com.tmap.mit.map_viewer.feature.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class Feature {
    private Geometry geometry;
    private List<Geometry> geometryList;
    private final Properties properties;

    public Feature(Geometry geometry, Properties properties) {
        this.geometry = geometry;
        this.properties = properties;
    }

    public Feature(List<Geometry> geometryList, Properties properties) {
        this.geometryList = geometryList;
        this.properties = properties;
    }
}
