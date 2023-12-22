package com.tmap.mit.map_viewer.cd;

import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Getter
public enum CaffeineCacheType {
    MAP_DATA("getMapDataByShapeFile", 5, 5, TimeUnit.SECONDS);

    CaffeineCacheType(String cacheName, int expiredAfterWrite, int maximumSize, TimeUnit timeUtint) {
        this.cacheName = cacheName;
        this.expiredAfterWrite = expiredAfterWrite;
        this.maximumSize = maximumSize;
        this.timeUtint = timeUtint;

    }

    private final String cacheName;
    private final int expiredAfterWrite;
    private final int maximumSize;
    private final TimeUnit timeUtint;

}
