package com.tmap.mit.map_viewer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableCaching
@SpringBootApplication
public class MapViewerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MapViewerApplication.class, args);
    }

}
