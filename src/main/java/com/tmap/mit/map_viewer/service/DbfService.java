package com.tmap.mit.map_viewer.service;

import com.tmap.mit.map_viewer.cd.ShapeType;
import com.tmap.mit.map_viewer.constant.ShpFile;
import com.tmap.mit.map_viewer.dto.ShpDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public interface DbfService {
    ShpDto.ResData getDbfParserDataWithCache(String fileName) throws IOException ;

    ShpDto.ResData getDbfParserDataNoCache(String fileName) throws IOException;

}