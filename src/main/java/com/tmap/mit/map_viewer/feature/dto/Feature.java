package com.tmap.mit.map_viewer.feature.dto;

import lombok.Getter;

@Getter
public class Feature {
    private final Geometry geometry;
    private final Properties properties;

    public Feature(Geometry geometry, Properties properties) {
        this.geometry = geometry;
        this.properties = properties;
    }
}
