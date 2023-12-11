package com.tmap.mit.map_viewer.controller;

import com.tmap.mit.map_viewer.dto.Point;
import com.tmap.mit.map_viewer.service.MapDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

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
