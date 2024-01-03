package com.tmap.mit.map_viewer.service.impl;

import com.tmap.mit.map_viewer.dto.ShapeDataDto;
import com.tmap.mit.map_viewer.dto.DbfDto;
import com.tmap.mit.map_viewer.dto.ShpDto;
import com.tmap.mit.map_viewer.dto.ShxDto;
import com.tmap.mit.map_viewer.service.DbfParserService;
import com.tmap.mit.map_viewer.service.ShapeDataService;
import com.tmap.mit.map_viewer.service.ShpParserService;
import com.tmap.mit.map_viewer.service.ShxParserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShapeDataServiceImpl  {
    private final ShpParserService shpParserService;
    private final ShxParserService shxParserService;
    private final DbfParserService dbfParserService;

   /* @Override
    public ShapeDataDto getShapeFileDataWithCache(String fileName) throws IOException {
        return getShapeFileDataNoCache(fileName);
    }

    @Override
    public ShapeDataDto getShapeFileDataNoCache(String fileName) throws IOException {
        ShpDto.ResData shpRes = shpParserService.getShpParserDataWithCache(fileName);
        DbfDto.ResData dbfRes = dbfParserService.getDbfParserDataWithCache(fileName);

        List<ShapeDataDto> shapeDataDataDtos = new ArrayList<>();

        for(int i=0; i<shpRes.getPoints().size(); i++){
            shapeDataDataDtos.add(new ShapeDataDto(shpRes.getPoints().get(i), dbfRes.getRecords().get(i)));
        }

        return shapeDataDataDtos;
    }*/
}
