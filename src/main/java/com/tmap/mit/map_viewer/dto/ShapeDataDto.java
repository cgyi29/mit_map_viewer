package com.tmap.mit.map_viewer.dto;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class ShapeDataDto {
    private ShpDto.Point geometry;
    private Map<String, Object> attributes;

    public ShapeDataDto(ShpDto.Point geometry, Map<String, Object> attributes){
        this.geometry = geometry;
        this.attributes = attributes;
    }
}
