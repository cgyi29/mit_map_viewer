package com.tmap.mit.map_viewer.legacy.dto;

import lombok.Getter;

@Getter
public class FeatureDto {
    private ShpDto.ResData geometry;
    private DbfDto.ResData property;

    public FeatureDto(ShpDto.ResData geometry, DbfDto.ResData property){
        this.geometry = geometry;
        this.property = property;
    }
}
