package com.tmap.mit.map_viewer.constant;

public class DbfFile {
    public static final String DBF_FILE_PATH_FORMAT = "files/%s.dbf";
    // header
    public static final int HEADER_SIZE = 100;
    public static final int HEADER_LENGTH = 24;
    public static final int IDX_HEADER_VERSION = 28;
    public static final int IDX_HEADER_SHAPE_TYPE = 32;

    // record
    public static final int RECORD_SIZE = 8; // (인덱스 레코드 크기 offset + length)
}
