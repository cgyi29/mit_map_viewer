package com.tmap.mit.map_viewer.feature.dto;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class Properties {
    private final Map<String, Object> combinedInfo;

    public Properties(Map<String, Object> combinedInfo) {
        this.combinedInfo = combinedInfo;
    }
}
