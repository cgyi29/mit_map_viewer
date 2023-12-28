package com.tmap.mit.map_viewer.service;

import com.tmap.mit.map_viewer.dto.DbfDto;
import com.tmap.mit.map_viewer.dto.ShpDto;

import java.io.IOException;

public interface DbfParserService {
    DbfDto.ResData getDbfParserDataWithCache(String fileName) throws IOException ;

    DbfDto.ResData getDbfParserDataNoCache(String fileName) throws IOException;

}