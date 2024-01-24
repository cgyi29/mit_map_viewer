package com.tmap.mit.map_viewer.feature.controller;

import com.tmap.mit.map_viewer.feature.dto.FeatureCollection;
import com.tmap.mit.map_viewer.feature.service.FeatureCollectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/map")
public class FeatureCollectionController {
    private final FeatureCollectionService featureCollectionService;

    @GetMapping("/feature/{countryName}")
    public ResponseEntity<FeatureCollection> getFeatureCollectionByCountryName(@PathVariable String countryName) {
        return  ResponseEntity.ok().body(featureCollectionService.getFeatureCollectionByCountryName(countryName));
    }
}
