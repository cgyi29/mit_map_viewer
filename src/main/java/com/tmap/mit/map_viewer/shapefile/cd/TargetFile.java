package com.tmap.mit.map_viewer.shapefile.cd;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TargetFile {
    KoyMahalle("point"),
    Duraklar("polygone"),
    Hatlar("polyline");

    private final String type;

}
