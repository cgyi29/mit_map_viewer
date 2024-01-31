package com.tmap.mit.map_viewer.cd;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Getter
@AllArgsConstructor
public enum CaffeineCacheType {
    SHP_PARSER_DATA("getShpParserData", "getShpParserData", "%s:%s", 10, 3, 5, TimeUnit.SECONDS),
    SHX_PARSER_DATA("getShxParserData", "getShxParserData", "%s:%s", 10, 3, 5, TimeUnit.SECONDS),
    DBF_PARSER_DATA("getDbfParserData", "getDbfParserData", "%s:%s", 10, 3, 5, TimeUnit.SECONDS),
    FEATURE_COLLECTION("getFeatureCollectionByCountry", "getFeatureCollectionByCountry", "%s:%s", 10, 3, 5, TimeUnit.SECONDS);

    private final String name;
    private final String key;
    private final String keyFormat;
    private final int expiredAfterWrite;
    private final int refreshAfterWrite;
    private final int maximumSize;
    private final TimeUnit timeUtint;

}
