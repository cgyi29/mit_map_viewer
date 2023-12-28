package com.tmap.mit.map_viewer.service;

import com.tmap.mit.map_viewer.dto.ShpDto;

import java.io.IOException;

public interface ShpParserService {
    ShpDto.ResData getShpParserDataWithCache(String fileName) throws IOException ;

    ShpDto.ResData getShpParserDataNoCache(String fileName) throws IOException;
}