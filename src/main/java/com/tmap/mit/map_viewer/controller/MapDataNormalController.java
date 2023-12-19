package com.tmap.mit.map_viewer.controller;

import com.tmap.mit.map_viewer.dto.ShapeData;
import com.tmap.mit.map_viewer.service.MapDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.IOException;

/**
 * map data controller
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mapData/normal")
public class MapDataNormalController {
    private final MapDataService mapDataService;
    @GetMapping("/{fileName}")
    public ShapeData getMapDataByShapeFile(@PathVariable String fileName) throws IOException {
        return mapDataService.getMapDataByShapeFile(fileName);
    }
}
