package com.tmap.mit.map_viewer.controller;

import com.tmap.mit.map_viewer.dto.ShpDto;
import com.tmap.mit.map_viewer.service.ShpParserServic;
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
    private ShpParserServic mapSHPDataService;

    private final String GET_MAP_DATA_URI_FORMMAT = "/mapData/%s";
    private final String POINT_FILE_NAME = "Duraklar";

    @Test
    @DisplayName("shape file에서 map data를 추출하는 api 컨트롤러 테스트")
    void givenFileName_whenGetMapDataByShapeFile_thenResponseCheck() throws IOException {
        // given
        String fileName = POINT_FILE_NAME;

        // when & then
        ShpDto.ResData mapData = mapSHPDataService.getShpParserDataNoCache(fileName);
        when(mapSHPDataService.getShpParserDataNoCache(POINT_FILE_NAME)).thenReturn(mapData);

        webTestClient.get().uri(String.format(GET_MAP_DATA_URI_FORMMAT, POINT_FILE_NAME))
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @Test
    @DisplayName("caffeine cache 적용 테스트 - controller 다건 호출 시 호출 시간 체크")
    void givenFileName_whenGetMapDataByShapeFile_thenShortRequestTime(){

    }

    @Test
    @DisplayName("caffeine cache 적용 테스트 - cache 만료 시간 이후 자동 refresh 되었는지 체크")
    void givenFileName_whenGetMapDataByShapeFile_thenAfterTTlApplyRefresh(){

    }
}