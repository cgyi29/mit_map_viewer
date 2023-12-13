package com.tmap.mit.map_viewer.controller;

import com.tmap.mit.map_viewer.dto.ShapeData;
import com.tmap.mit.map_viewer.service.MapDataService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;

import static org.mockito.Mockito.when;

@WebFluxTest(MapDataController.class)
@AutoConfigureWebTestClient
class MapDataControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private MapDataService mapDataService;

    private final String GET_MAP_DATA_URI_FORMMAT = "/mapData/%s";
    private final String POINT_FILE_NAME = "Duraklar";

    @Test
    @DisplayName("shape file에서 map data를 추출하는 api 컨트롤러 테스트")
    void givenFileName_whenGetMapDataByShapeFile_thenResponseCheck() throws IOException {
        // given
        String fileName = POINT_FILE_NAME;

        // when & then
        ShapeData mapData = mapDataService.getMapDataByShapeFile(fileName);
        when(mapDataService.getMapDataByShapeFile(POINT_FILE_NAME)).thenReturn(mapData);

        webTestClient.get().uri(String.format(GET_MAP_DATA_URI_FORMMAT, POINT_FILE_NAME))
                .exchange()
                .expectStatus().is2xxSuccessful();
    }
}