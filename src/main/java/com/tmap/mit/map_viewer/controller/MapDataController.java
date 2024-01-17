package com.tmap.mit.map_viewer.controller;

import com.tmap.mit.map_viewer.dto.DbfDto;
import com.tmap.mit.map_viewer.dto.ShpDto;
import com.tmap.mit.map_viewer.service.DbfParserServic;
import com.tmap.mit.map_viewer.service.ShpParserServic;
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
    private final ShpParserServic shpParserService;
    private final DbfParserServic dbfParserServic;

    @GetMapping("/shp/{fileName}")
    public ResponseEntity<ShpDto.ResData> getShpParserDataByShapeFile(@PathVariable String fileName) throws IOException {
        return  ResponseEntity.ok().body(shpParserService.getShpParserDataWithCache(fileName));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/dbf/{fileName}")
    public ResponseEntity<DbfDto.ResData> getDbfParserDataByShapeFile(@PathVariable String fileName) throws IOException {
        return  ResponseEntity.ok().body(dbfParserServic.getDbfParserDataWithCache(fileName));
    }
}
