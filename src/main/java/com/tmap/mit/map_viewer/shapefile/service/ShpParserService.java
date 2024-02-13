package com.tmap.mit.map_viewer.shapefile.service;

import com.tmap.mit.map_viewer.shapefile.cd.ShapeType;
import com.tmap.mit.map_viewer.shapefile.constant.ShpFile;
import com.tmap.mit.map_viewer.shapefile.dto.ShpDto;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ShpParserService {
    @Cacheable(cacheNames = "getShpParserData", key = "'getShpParserData:'+#fileName")
    public ShpDto.ResData getShpParserDataWithCache(String path, String fileName) throws IOException {
        return this.getShpParserDataNoCache(path, fileName);
    }

    public ShpDto.ResData getShpParserDataNoCache(String path, String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource(String.format(ShpFile.SHP_FILE_PATH_FORMAT_NEW, path, fileName));
        try (FileChannel channel = new FileInputStream(resource.getFile()).getChannel()) {
            MappedByteBuffer headerBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, ShpFile.HEADER_SIZE);
            headerBuffer.order(ByteOrder.LITTLE_ENDIAN);

            int shapeTypeCode = headerBuffer.getInt(ShpFile.IDX_HEADER_SHAPE_TYPE);
            ShpDto.BoundingBox bbox = new ShpDto.BoundingBox(
                    headerBuffer.getDouble(ShpFile.IDX_HEADER_MIN_X), headerBuffer.getDouble(ShpFile.IDX_HEADER_MIN_Y),
                    headerBuffer.getDouble(ShpFile.IDX_HEADER_MAX_X), headerBuffer.getDouble(ShpFile.IDX_HEADER_MAX_Y));

            Map<Integer, List<ShpDto.CoordinateInfo>> coordinateInfoMap = new HashMap<>();
            List<ShpDto.CoordinateInfo> coordinateInfo = new ArrayList<>();
            List<ShpDto.BoundingBox> recordBboxs = new ArrayList<>();
            int[] parts = new int[0];
            int polygonIdx = 0;

            long position = ShpFile.HEADER_SIZE;
            while (position < channel.size()) {
                MappedByteBuffer recordBuffer = channel.map(FileChannel.MapMode.READ_ONLY, position, ShpFile.RECORD_HEADER_SIZE);
                recordBuffer.order(ByteOrder.BIG_ENDIAN);

                if (recordBuffer.remaining() < ShpFile.RECORD_HEADER_SIZE) break;
                int contentLength = recordBuffer.getInt(ShpFile.RECORD_HEADER_LENGTH) * 2;
                MappedByteBuffer contentBuffer = channel.map(FileChannel.MapMode.READ_ONLY, position + ShpFile.RECORD_HEADER_SIZE, contentLength);
                contentBuffer.order(ByteOrder.LITTLE_ENDIAN);


                List<ShpDto.Coordinates> coordinates = new ArrayList<>();
                if (ShapeType.POINT.getCode().equals(shapeTypeCode)) {
                    coordinates.add(new ShpDto.Coordinates(
                            contentBuffer.getDouble(ShpFile.IDX_RECORD_CONTENT_POINT_TYPE_X),
                            contentBuffer.getDouble(ShpFile.IDX_RECORD_CONTENT_POINT_TYPE_Y)));
                    coordinateInfo.add(new ShpDto.CoordinateInfo(coordinates, parts, recordBboxs));
                }

                if (ShapeType.POLYLINE.getCode().equals(shapeTypeCode)) {
                    recordBboxs.add(new ShpDto.BoundingBox(
                            contentBuffer.getDouble(ShpFile.IDX_RECORD_CONTENT_POLY_MIN_X),
                            contentBuffer.getDouble(ShpFile.IDX_RECORD_CONTENT_POLY_MIN_Y),
                            contentBuffer.getDouble(ShpFile.IDX_RECORD_CONTENT_POLY_MAX_X),
                            contentBuffer.getDouble(ShpFile.IDX_RECORD_CONTENT_POLY_MAX_Y)));

                    int numParts = contentBuffer.getInt(ShpFile.IDX_RECORD_CONTENT_NUM_PARTS);
                    int numPoints = contentBuffer.getInt(ShpFile.IDX_RECORD_CONTENT_NUM_POINTS);

                    parts = new int[numParts];
                    int idxPartsStart = ShpFile.IDX_RECORD_CONTENT_PARTS;
                    for (int i = 0; i < numParts; i++) {
                        parts[i] = contentBuffer.getInt(idxPartsStart);
                        idxPartsStart += Integer.BYTES;
                    }

                    List<ShpDto.Coordinates> polyPoints = new ArrayList<>();
                    int idxPolyX = ShpFile.IDX_RECORD_CONTENT_POLY_X;
                    int idxPolyY = ShpFile.IDX_RECORD_CONTENT_POLY_Y;
                    for (int i = 0; i < numPoints; i++) {
                        double x = contentBuffer.getDouble(idxPolyX);
                        double y = contentBuffer.getDouble(idxPolyY);
                        idxPolyX += Double.BYTES * 2;
                        idxPolyY += Double.BYTES * 2;
                        polyPoints.add(new ShpDto.Coordinates(x, y));
                    }
                    coordinateInfo.add(new ShpDto.CoordinateInfo(polyPoints, parts, recordBboxs));
                }

                if (ShapeType.POLYGON.getCode().equals(shapeTypeCode)) {
                    recordBboxs.add(new ShpDto.BoundingBox(
                            contentBuffer.getDouble(ShpFile.IDX_RECORD_CONTENT_POLY_MIN_X),
                            contentBuffer.getDouble(ShpFile.IDX_RECORD_CONTENT_POLY_MIN_Y),
                            contentBuffer.getDouble(ShpFile.IDX_RECORD_CONTENT_POLY_MAX_X),
                            contentBuffer.getDouble(ShpFile.IDX_RECORD_CONTENT_POLY_MAX_Y)));


                    int numParts = contentBuffer.getInt(ShpFile.IDX_RECORD_CONTENT_NUM_PARTS);
                    int numPoints = contentBuffer.getInt(ShpFile.IDX_RECORD_CONTENT_NUM_POINTS);
                    parts = new int[numParts];
                    for (int i = 0; i < numParts; i++) {
                        parts[i] = contentBuffer.getInt(ShpFile.IDX_RECORD_CONTENT_PARTS + i * Integer.BYTES);
                    }


                    List<ShpDto.CoordinateInfo> partCoordinateInfoList = new ArrayList<>();
                    int idxPointStart = ShpFile.IDX_RECORD_CONTENT_PARTS + numParts * Integer.BYTES;
                    for (int partIndex = 0; partIndex < numParts; partIndex++) {
                        int startIdx = parts[partIndex];
                        int endIdx = (partIndex < numParts - 1) ? parts[partIndex + 1] : numPoints;


                        List<ShpDto.Coordinates> polyPoints = new ArrayList<>();
                        for (int i = startIdx; i < endIdx; i++) {
                            double x = contentBuffer.getDouble(idxPointStart + i * Double.BYTES * 2);
                            double y = contentBuffer.getDouble(idxPointStart + i * Double.BYTES * 2 + Double.BYTES);
                            polyPoints.add(new ShpDto.Coordinates(x, y));
                        }
                        partCoordinateInfoList.add(new ShpDto.CoordinateInfo(polyPoints, new int[]{partIndex}, recordBboxs));
                    }
                    coordinateInfoMap.put(polygonIdx++, partCoordinateInfoList); // 각 Polygon 레코드마다 part별 CoordinateInfo를 map에 추가
                }


                position += ShpFile.RECORD_HEADER_SIZE + contentLength;
            }

            return new ShpDto.ResData(ShapeType.getShapeTypeByCode(shapeTypeCode).name(), bbox, coordinateInfo, coordinateInfoMap);
        }
    }
}