package com.tmap.mit.map_viewer.feature.dto;

import com.tmap.mit.map_viewer.shapefile.dto.ShpDto;
import lombok.Getter;

import java.util.List;

@Getter
public class Geometry {
    private final String type;
    private final ShpDto.CoordinateInfo coordinatesInfo;
    private final ShpDto.BoundingBox bbox;
    private ShpDto.BoundingBox largestBbox;
    public Geometry(String type, ShpDto.CoordinateInfo coordinatesInfo, ShpDto.BoundingBox bbox) {
        this.type = type;
        this.coordinatesInfo = coordinatesInfo;
        this.bbox = bbox;
    }

    public Geometry(String type, ShpDto.CoordinateInfo coordinatesInfo, ShpDto.BoundingBox bbox, ShpDto.BoundingBox largestBbox) {
        this.type = type;
        this.coordinatesInfo = coordinatesInfo;
        this.bbox = bbox;
        this.largestBbox = largestBbox;
    }

}
