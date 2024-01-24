package com.tmap.mit.map_viewer.feature.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class FeatureCollection {
    private final List<Feature>  featureCollection;

    public FeatureCollection(List<Feature> featureCollection) {
        this.featureCollection = featureCollection;
    }
}
