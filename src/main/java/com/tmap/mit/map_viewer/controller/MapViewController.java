package com.tmap.mit.map_viewer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

/**
 * map view controller
 */
@Controller
public class MapViewController {
    @GetMapping("")
    public Mono<String> getView() {
        return Mono.just("mapViewer");
    }
}
