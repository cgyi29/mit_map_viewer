package com.tmap.mit.map_viewer.dto;

import lombok.Getter;

@Getter
public class FieldMetaData {
    private String name;
    private char type;
    private int length;

    public FieldMetaData(String name, char type, int length){
        this.name = name;
        this.type = type;
        this.length = length;
    }
}
