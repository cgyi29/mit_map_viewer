package com.tmap.mit.map_viewer.legacy.controller;

import com.tmap.mit.map_viewer.legacy.dto.DbfDto;
import com.tmap.mit.map_viewer.legacy.service.DbfParserServiceV1;
import com.tmap.mit.map_viewer.legacy.service.ShpParserServiceV1;
import com.tmap.mit.map_viewer.legacy.dto.FeatureDto;
import com.tmap.mit.map_viewer.legacy.dto.ShpDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * map data controller
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/map")
public class MapDataController {
    private final ShpParserServiceV1 shpParserService;
    private final DbfParserServiceV1 dbfParserService;

    @GetMapping("/feature/{fileName}")
    public ResponseEntity<FeatureDto> getShpParserDataByShapeFileV1(@PathVariable String fileName) throws IOException {
        ShpDto.ResData geometry = shpParserService.getShpParserDataWithCache(fileName);
        DbfDto.ResData property = dbfParserService.getDbfParserDataWithCache(fileName);
        return  ResponseEntity.ok().body(new FeatureDto(geometry, property));
    }
}
