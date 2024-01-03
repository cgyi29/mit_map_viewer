package com.tmap.mit.map_viewer.service;

import com.tmap.mit.map_viewer.dto.ShapeDataDto;

import java.io.IOException;

public interface ShapeDataService {
    ShapeDataDto getShapeFileDataWithCache(String fileName) throws IOException;
    ShapeDataDto getShapeFileDataNoCache(String fileName) throws IOException;
}
