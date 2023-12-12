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

/**
 * map data controller
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mapData")
public class MapDataController {
    private final MapDataService mapDataService;

    @GetMapping("/{fileName}")
    public Mono<ShapeData> getMapDataByShapeFile(@PathVariable String fileName) {
        return Mono.fromCallable(() -> mapDataService.getMapDataByShapeFile(fileName));
    }
}
