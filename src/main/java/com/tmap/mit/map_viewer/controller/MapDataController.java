package com.tmap.mit.map_viewer.controller;

import com.tmap.mit.map_viewer.dto.DbfDto;
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
    private final DbfParserService dbfparserService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/shp/{fileName}")
    public ShpDto.ResData getShpParserDataByShapeFile(@PathVariable String fileName) throws IOException {
        return  shpParserService.getShpParserDataWithCache(fileName);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/dbf/{fileName}")
    public DbfDto.ResData getDbfParserDataByShapeFile(@PathVariable String fileName) throws IOException {
        return  dbfparserService.getDbfParserDataWithCache(fileName);
    }
}
