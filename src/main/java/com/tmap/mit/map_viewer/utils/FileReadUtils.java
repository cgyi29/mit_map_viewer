package com.tmap.mit.map_viewer.utils;

import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class FileReadUtils {
    public static MappedByteBuffer initializeBuffer(String fileFormat, String fileName, ByteOrder byteOrder) throws IOException {
        ClassPathResource resource = new ClassPathResource(String.format(fileFormat, fileName));
        try (FileChannel channel = new FileInputStream(resource.getFile()).getChannel()) {
            return (MappedByteBuffer) channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size()).order(byteOrder);
        }
    }



}
