package com.tmap.mit.map_viewer.service;

import com.tmap.mit.map_viewer.dto.ShpDto;
import com.tmap.mit.map_viewer.dto.ShxDto;

import java.io.IOException;

public interface ShxParserService {
    ShxDto.ResData getShxParserDataWithCache(String fileName) throws IOException ;

    ShxDto.ResData getShxParserDataNoCache(String fileName) throws IOException;
}