package com.tmap.mit.map_viewer.cd;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Getter
@AllArgsConstructor
public enum CaffeineCacheType {
    MAP_DATA("getMapDataByShapeFile", "getMapDataByShapeFile", "%s:%s", 10, 3, 5, TimeUnit.SECONDS);

    private final String name;
    private final String key;
    private final String keyFormat;
    private final int expiredAfterWrite;
    private final int refreshAfterWrite;
    private final int maximumSize;
    private final TimeUnit timeUtint;

}
