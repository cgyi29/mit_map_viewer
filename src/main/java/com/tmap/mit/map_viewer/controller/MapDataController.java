package com.tmap.mit.map_viewer.controller;

import com.tmap.mit.map_viewer.dto.DbfDto;
import com.tmap.mit.map_viewer.dto.FeatureDto;
import com.tmap.mit.map_viewer.dto.ShpDto;
import com.tmap.mit.map_viewer.service.DbfParserService;
import com.tmap.mit.map_viewer.service.ShpParserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * map data controller
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mapData")
public class MapDataController {
    private final ShpParserService shpParserService;
    private final DbfParserService dbfParserService;

    @GetMapping("/shp/{fileName}")
    public ResponseEntity<FeatureDto> getShpParserDataByShapeFile(@PathVariable String fileName) throws IOException {
        ShpDto.ResData geometry = shpParserService.getShpParserDataWithCache(fileName);
        DbfDto.ResData property = dbfParserService.getDbfParserDataWithCache(fileName);
        return  ResponseEntity.ok().body(new FeatureDto(geometry, property));
    }
}
