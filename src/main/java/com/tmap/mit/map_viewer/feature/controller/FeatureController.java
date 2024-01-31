package com.tmap.mit.map_viewer.feature.controller;

import com.tmap.mit.map_viewer.annotation.CountryValid;
import com.tmap.mit.map_viewer.feature.dto.FeatureCollection;
import com.tmap.mit.map_viewer.feature.service.FeatureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/map")
public class FeatureController {
    private final FeatureService featureCollectionService;

    @GetMapping("/feature/{country}/{x}/{y}")
    public ResponseEntity<FeatureCollection> getFeatureCollectionByCountryName(@CountryValid @PathVariable String country, @PathVariable double x, @PathVariable double y) {
        String countryUpper = country.toLowerCase();
        return  ResponseEntity.ok().body(featureCollectionService.getFeatureCollectionByCountryNameWithCache(countryUpper, x, y));
    }
}
