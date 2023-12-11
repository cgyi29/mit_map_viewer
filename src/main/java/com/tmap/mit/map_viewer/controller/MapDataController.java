package com.tmap.mit.map_viewer.controller;

import com.tmap.mit.map_viewer.dto.Point;
import com.tmap.mit.map_viewer.service.MapDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    public Flux<Point> createByShapeFile(@PathVariable String fileName) {
        try{
            return Flux.fromIterable(mapDataService.createByShapeFile(fileName))
                    .subscribeOn(Schedulers.boundedElastic());
        }catch (IOException e){
            return Flux.error(e);
        }
    }


}
