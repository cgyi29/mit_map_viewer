package com.tmap.mit.map_viewer.init.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

/**
 * map view controller
 */
@Controller
public class WelcomeController {
    @GetMapping("/v2")
    public Mono<String> getViewNew() {
        return Mono.just("mapViewer");
    }
}
