package com.tmap.mit.map_viewer.service;

import com.tmap.mit.map_viewer.dto.DbfDto;
import com.tmap.mit.map_viewer.dto.ShapeDataDto;

import java.io.IOException;
import java.util.List;

public interface ShapeDataService {
    List<ShapeDataDto> getShapeFileDataWithCache(String fileName) throws IOException;
    List<ShapeDataDto> getShapeFileDataNoCache(String fileName) throws IOException;
}
