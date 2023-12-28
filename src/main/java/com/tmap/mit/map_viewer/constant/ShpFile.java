package com.tmap.mit.map_viewer.constant;

public class ShpFile {
    public static final String SHP_FILE_PATH_FORMAT = "files/%s.shp";
    // file header
    public static final int HEADER_SIZE = 100;
    public static final int HEADER_LENGTH = 24;
    public static final int IDX_HEADER_VERSION = 28;
    public static final int IDX_HEADER_SHAPE_TYPE = 32;
    public static final int IDX_HEADER_MIN_X = 36;
    public static final int IDX_HEADER_MIN_Y = 44;
    public static final int IDX_HEADER_MAX_X = 52;
    public static final int IDX_HEADER_MAX_Y = 60;

    // record header
    public static final int RECORD_HEADER_SIZE = 8;
    public static final int RECORD_HEADER_LENGTH = 4;

    // record content
    public static final int IDX_RECORD_CONTENT_SHAPE_TYPE = 0;
    public static final int IDX_RECORD_CONTENT_POINT_TYPE_X = 4;
    public static final int IDX_RECORD_CONTENT_POINT_TYPE_Y = 12;
    public static final int IDX_RECORD_CONTENT_POLY_MIN_X = 4;
    public static final int IDX_RECORD_CONTENT_POLY_MIN_Y = 12;
    public static final int IDX_RECORD_CONTENT_POLY_MAX_X = 20;
    public static final int IDX_RECORD_CONTENT_POLY_MAX_Y = 28;
    public static final int IDX_RECORD_CONTENT_NUM_PARTS = 36;
    public static final int IDX_RECORD_CONTENT_NUM_POINTS = 40;
    public static final int IDX_RECORD_CONTENT_PARTS = 44;
    public static final int IDX_RECORD_CONTENT_POLY_X = 48;
    public static final int IDX_RECORD_CONTENT_POLY_Y = 56;

}
