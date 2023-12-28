package com.tmap.mit.map_viewer.service;

import com.tmap.mit.map_viewer.dto.ShpDto;

import java.io.IOException;

public interface ShxParserService {
    ShpDto.ResData getShxParserDataWithCache(String fileName) throws IOException ;

    ShpDto.ResData getShxParserDataNoCache(String fileName) throws IOException;
}