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

    /**
     * 타입 별 값 변환
     * @param type
     * @param dataString
     * @return
     */
    public static Object convertDataStringByType(char type, String dataString) {
        if(FieldType.D.getCharType()== type && dataString.length() == 8){
            int year = Integer.parseInt(dataString.substring(0, 4));
            int month = Integer.parseInt(dataString.substring(4, 6));
            int day = Integer.parseInt(dataString.substring(6, 8));
            return LocalDate.of(year, month, day);
        }

        if(FieldType.F.getCharType()== type){
            return Float.parseFloat(dataString);
        }

        if(FieldType.N.getCharType()== type){
            return dataString.contains(".")?Double.parseDouble(dataString):Integer.parseInt(dataString);
        }

        if(FieldType.L.getCharType()== type){
            char ch = dataString.toUpperCase().charAt(0);
            return ch == 'T' || ch == 'Y';
        }

        // C/M and Ect
        return dataString;

    }
}
