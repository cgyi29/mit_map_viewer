package com.tmap.mit.map_viewer.feature.service;

import com.tmap.mit.map_viewer.shapefile.dto.ShpDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataConvertService {
    public ShpDto.CoordinateInfo  convertCoordinate(ShpDto.CoordinateInfo coordinateInfo, ShpDto.BoundingBox bbox, ShpDto.BoundingBox largestBbox, double drawX, double drawY) {
        List<ShpDto.Coordinates> scaledCoordinates = coordinateInfo.getCoordinates().parallelStream()
                .map(coordinates -> scaleCoordinate(coordinates.getX(), coordinates.getY(), bbox, largestBbox, drawX, drawY))
                .toList();
        return new ShpDto.CoordinateInfo(scaledCoordinates, coordinateInfo.getParts(), coordinateInfo.getRecordBboxs());

    }

    private ShpDto.Coordinates scaleCoordinate(double x, double y, ShpDto.BoundingBox bbox, ShpDto.BoundingBox largestBbox, double drawX, double drawY) {
        double transformX, transformY;
        if (isBboxEqual(bbox, largestBbox)) {
            transformX = ((bbox.getMaxX() - x) / (bbox.getMaxX() - bbox.getMinX())) * drawX;
            transformY = drawY - ((bbox.getMaxY() - y) / (bbox.getMaxY() - bbox.getMinY())) * drawY;
        } else {
            ShpDto.BoundingBox adjustedBbox = adjustBboxScale(bbox, largestBbox);
            transformX = ((adjustedBbox.getMaxX() - x) / (adjustedBbox.getMaxX() - adjustedBbox.getMinX())) * drawX;
            transformY = drawY - ((adjustedBbox.getMaxY() - y) / (adjustedBbox.getMaxY() - adjustedBbox.getMinY())) * drawY;
        }
        transformX = drawX - transformX;
        transformY = drawY - transformY;
        return new ShpDto.Coordinates(transformX, transformY);
    }

    private boolean isBboxEqual(ShpDto.BoundingBox bbox, ShpDto.BoundingBox largestBbox) {
        return bbox.getMinX() == largestBbox.getMinX() && bbox.getMinY() == largestBbox.getMinY()
                && bbox.getMaxX() == largestBbox.getMaxX() && bbox.getMaxY() == largestBbox.getMaxY();
    }

    private ShpDto.BoundingBox adjustBboxScale(ShpDto.BoundingBox bbox, ShpDto.BoundingBox largestBbox) {
        double scaleX = (largestBbox.getMaxX() - largestBbox.getMinX()) / (bbox.getMaxX() - bbox.getMinX());
        double scaleY = (largestBbox.getMaxY() - largestBbox.getMinY()) / (bbox.getMaxY() - bbox.getMinY());
        return new ShpDto.BoundingBox(
                largestBbox.getMinX() + (0.0) * scaleX,
                largestBbox.getMinY() + (0.0) * scaleY,
                largestBbox.getMinX() + (bbox.getMaxX() - bbox.getMinX()) * scaleX,
                largestBbox.getMinY() + (bbox.getMaxY() - bbox.getMinY()) * scaleY
        );
    }
}
