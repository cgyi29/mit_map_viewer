package com.tmap.mit.map_viewer.cd;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.checkerframework.checker.units.qual.A;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public enum FieldType {
    C('C', "Character"),
    D('D', "Assuming the format is YYYYMMDD"),
    F('F', "Floating point"),
    N('N', "Numeric"),
    L('L', "Logical"),
    M('M', "Memo");

    private final char charType;
    private final String desc;
}
