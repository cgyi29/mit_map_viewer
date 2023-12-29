package com.tmap.mit.map_viewer.controller;

import com.tmap.mit.map_viewer.dto.ShpDto;
import com.tmap.mit.map_viewer.service.ShpParserService;
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
@RequestMapping("/mapData")
public class MapDataController {
    private final ShpParserService shpParserService;

    @GetMapping("/{fileName}")
    public ResponseEntity<ShpDto.ResData> getMapDataByShapeFile(@PathVariable String fileName) throws IOException {
        return  ResponseEntity.ok(shpParserService.getShpParserDataWithCache(fileName));
    }
}
