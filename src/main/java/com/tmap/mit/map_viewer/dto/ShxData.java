package com.tmap.mit.map_viewer.dto;

import com.tmap.mit.map_viewer.constant.ShxRecord;
import lombok.Getter;

import java.util.List;

@Getter
public class ShxData {
    private int shapeType;
    List<ShxRecordData> records;

    public ShxData(int shapeType, List<ShxRecordData> records){
        this.shapeType = shapeType;
        this.records = records;
    }
}
