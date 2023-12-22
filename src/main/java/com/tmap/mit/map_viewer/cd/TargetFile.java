package com.tmap.mit.map_viewer.cd;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Getter
@AllArgsConstructor
public enum TargetFile {
    KoyMahalle("shp", "point"),
    Duraklar("shp", "polygone"),
    Hatlar("shp", "polyline");

    private final String extention;
    private final String type;

}
