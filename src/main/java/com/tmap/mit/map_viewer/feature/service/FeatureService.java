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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ArrayUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FeatureService {
    private final ShpParserService shpParserService;
    private final DbfParserService dbfParserService;
    private final CompareService compareService;
    private final DataConvertService dataConvertService;

    @Cacheable(cacheNames = "getFeatureCollectionByCountry", key = "'getFeatureCollectionByCountry:'+#country+':'+#x+':'+#y")
    public FeatureCollection getFeatureCollectionByCountryNameWithCache(String country, double x, double y){
        return this.getFeatureCollectionByCountryNameNoCache(country, x, y);
    }

    public FeatureCollection getFeatureCollectionByCountryNameNoCache(String country, double x, double y){
        try{
            Resource resource = new ClassPathResource("files/"+country);
            File dir = resource.getFile();

            List<Geometry> geometryList = new ArrayList<>();
            Map<Integer, List<Geometry>> geometryMap = new HashMap<>();
            File[] shpFiles = getShpFileArray(dir);
            ShpDto.BoundingBox largestBbox = new ShpDto.BoundingBox(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, -Double.POSITIVE_INFINITY, -Double.POSITIVE_INFINITY);
            ShpDto.ResData shpData = null;
            List<ShpDto.ResData> shpDataList = new ArrayList<>();


            if(!ArrayUtils.isEmpty(shpFiles)){
                for(File file : shpFiles){
                    shpData = shpParserService.getShpParserDataWithCache(country, file.getName());
                    largestBbox = compareService.getLargestBbox(largestBbox, shpData.getBbox());
                    shpDataList.add(shpData);
                }

                for(ShpDto.ResData shpDt : shpDataList){
                    for(ShpDto.CoordinateInfo coordinateInfo: shpDt.getCoordinateInfo()){
                        geometryList.add(new Geometry(shpDt.getType(), dataConvertService.convertCoordinate(coordinateInfo, shpDt.getBbox(), largestBbox, x, y), shpDt.getBbox()));
                    }

                    Map<Integer, List<ShpDto.CoordinateInfo>> coordinateInfoMap = shpDt.getCoordinateInfoMap();
                    for(Map.Entry<Integer, List<ShpDto.CoordinateInfo>> entry : coordinateInfoMap.entrySet()){
                        List<Geometry> geometriesForRecord = new ArrayList<>();
                        for(ShpDto.CoordinateInfo ci : entry.getValue()){
                            geometriesForRecord.add(new Geometry(shpDt.getType(), dataConvertService.convertCoordinate(ci, shpDt.getBbox(), largestBbox, x, y), shpDt.getBbox()));
                        }
                        geometryMap.put(entry.getKey(), geometriesForRecord);
                    }
                }
            }

            List<Map<String, Object>> propertyList = new ArrayList<>();
            File[] dbfFiles = getDbfFileArray(dir);
            if(!ArrayUtils.isEmpty(shpFiles)){
                for(File file : dbfFiles){
                    DbfDto.ResData dbfData = dbfParserService.getDbfParserDataWithCache(country, file.getName());
                    propertyList.addAll(dbfData.getRecords());
                }
            }

            List<Feature> featureList = new ArrayList<>();
            int init = 0;
            for(Geometry geometry: geometryList){
                featureList.add(new Feature(new Geometry(geometry.getType(), geometry.getCoordinatesInfo(), geometry.getBbox(), largestBbox), new Properties(propertyList.get(init))));
                init ++;
            }

            for(Map.Entry<Integer, List<Geometry>> entry : geometryMap.entrySet()){
                if(init < propertyList.size()) {
                    List<Geometry> geometries = entry.getValue();
                    featureList.add(new Feature(geometries, new Properties(propertyList.get(init))));
                    init++;
                }
            }


            return new FeatureCollection(featureList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File[] getShpFileArray(File dir){
        File[] shpFiles = dir.listFiles((d, name) -> name.endsWith(FileExtention.shp.name()));
        Arrays.sort(shpFiles, (file1, file2) -> file1.getName().compareToIgnoreCase(file2.getName()));
        return shpFiles;
    }

    private File[] getDbfFileArray(File dir){
        File[] dbfFiles = dir.listFiles((d, name) -> name.endsWith(FileExtention.dbf.name()));
        Arrays.sort(dbfFiles, (file1, file2) -> file1.getName().compareToIgnoreCase(file2.getName()));
        return dbfFiles;
    }
}
