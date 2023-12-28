package com.tmap.mit.map_viewer.cd;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Getter
@AllArgsConstructor
public enum TargetFile {
    KoyMahalle(new String[]{"shp", "dbf", "shx"}, "point"),
    Duraklar(new String[]{"shp","dbf","shx"}, "polygone"),
    Hatlar(new String[]{"shp","dbf","shx"}, "polyline");

    private final String[] extention;
    private final String type;

}
