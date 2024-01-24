package com.tmap.mit.map_viewer.feature.service;

import com.tmap.mit.map_viewer.feature.dto.Feature;
import com.tmap.mit.map_viewer.feature.dto.FeatureCollection;
import com.tmap.mit.map_viewer.feature.dto.Geometry;
import com.tmap.mit.map_viewer.feature.dto.Properties;
import com.tmap.mit.map_viewer.shapefile.cd.FileExtention;
import com.tmap.mit.map_viewer.shapefile.dto.DbfDto;
import com.tmap.mit.map_viewer.shapefile.dto.ShpDto;
import com.tmap.mit.map_viewer.shapefile.service.DbfParserService;
import com.tmap.mit.map_viewer.shapefile.service.ShpParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ArrayUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FeatureCollectionService {
    private final ShpParserService shpParserService;
    private final DbfParserService dbfParserService;

    public FeatureCollection getFeatureCollectionByCountryName(String country){
        try{
            Resource resource = new ClassPathResource("files/"+country);
            File dir = resource.getFile();

            List<Geometry> geometryList = new ArrayList<>();
            List<Map<String, Object>> propertyList = new ArrayList<>();

            File[] shpFiles = dir.listFiles((d, name) -> name.endsWith(FileExtention.shp.name()));
            Arrays.sort(shpFiles, (file1, file2) -> file1.getName().compareToIgnoreCase(file2.getName()));
            if(!ArrayUtils.isEmpty(shpFiles)){
                for(File file : shpFiles){
                    ShpDto.ResData shpData = shpParserService.getShpParserDataWithCache(country, file.getName());
                    for(Object coordinates: shpData.getCoordinates()){
                        geometryList.add(new Geometry(shpData.getType().name(), coordinates));
                    }
                }
            }

            File[] dbfFiles = dir.listFiles((d, name) -> name.endsWith(FileExtention.dbf.name()));
            Arrays.sort(dbfFiles, (file1, file2) -> file1.getName().compareToIgnoreCase(file2.getName()));
            if(!ArrayUtils.isEmpty(shpFiles)){
                for(File file : dbfFiles){
                    DbfDto.ResData dbfData = dbfParserService.getDbfParserDataWithCache(country, file.getName());
                    propertyList.addAll(dbfData.getRecords());
                }
            }

            List<Feature> featureList = new ArrayList<>();
            int init = 0;
            for(Geometry geometry: geometryList){
                Map<String, Object> test =  propertyList.get(init);
                featureList.add(new Feature(new Geometry(geometry.getType(), geometry.getCoordinates()), new Properties(test)));
                init ++;
            }

            return new FeatureCollection(featureList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
