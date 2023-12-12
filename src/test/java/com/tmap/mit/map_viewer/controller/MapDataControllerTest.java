package com.tmap.mit.map_viewer.controller;

import com.tmap.mit.map_viewer.dto.ShapeData;
import com.tmap.mit.map_viewer.service.MapDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.mockito.Mockito.when;

@WebFluxTest(MapDataController.class)
@AutoConfigureWebTestClient
class MapDataControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private MapDataService mapDataService;

    private final String POINT_FILE_NAME = "Duraklar";
    private final String URI_PATH_FORMAT = "/mapData/%s";

    @Test
    void createByShapeFile(){
        when(mapDataService.createByShapeFile(POINT_FILE_NAME)).thenReturn(
                Flux.just(ShapeData.builder().build())
        );

        webTestClient.get().uri(String.format(URI_PATH_FORMAT, POINT_FILE_NAME))
                .exchange()
                .expectStatus().is2xxSuccessful();
    }
}