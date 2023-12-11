package com.tmap.mit.map_viewer.mapData.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class MapDataController {
    @GetMapping("/test")
    public Mono<String> hello() {
        return Mono.just("Hello");
    }
}
