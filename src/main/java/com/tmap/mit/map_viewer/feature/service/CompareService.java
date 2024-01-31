package com.tmap.mit.map_viewer.feature.service;

import com.tmap.mit.map_viewer.shapefile.dto.ShpDto;
import org.springframework.stereotype.Service;

@Service
public class CompareService {
    public ShpDto.BoundingBox getLargestBbox(ShpDto.BoundingBox targetBbox, ShpDto.BoundingBox compareBbox){
        return new ShpDto.BoundingBox(
                Math.min(targetBbox.getMinX(), compareBbox.getMinX()),
                Math.min(targetBbox.getMinY(), compareBbox.getMinY()),
                Math.max(targetBbox.getMaxX(), compareBbox.getMaxX()),
                Math.max(targetBbox.getMaxY(), compareBbox.getMaxY())
        );
    }
}
