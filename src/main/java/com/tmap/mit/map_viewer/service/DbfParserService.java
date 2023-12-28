package com.tmap.mit.map_viewer.service;

import com.tmap.mit.map_viewer.dto.ShpDto;

import java.io.IOException;

public interface DbfParserService {
    ShpDto.ResData getDbfParserDataWithCache(String fileName) throws IOException ;

    ShpDto.ResData getDbfParserDataNoCache(String fileName) throws IOException;

}