package com.tmap.mit.map_viewer.dto;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class FeatureDto {
    private ShpDto.ResData geometry;
    private DbfDto.ResData property;

    public FeatureDto(ShpDto.ResData geometry, DbfDto.ResData property){
        this.geometry = geometry;
        this.property = property;
    }
}
