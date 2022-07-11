package com.example.onlinechat.util;

import com.example.onlinechat.exception.UnsupportedImageType;

public class Util {
    public static String getFileExtension(String originalFilename) {
        final int dotPosition = originalFilename.lastIndexOf(".");
        if (dotPosition == -1)
            throw new UnsupportedImageType();
        final String extension = originalFilename.substring(dotPosition + 1);
        if (!extension.equals("jpg") && !extension.equals("png"))
            throw new UnsupportedImageType();
        return extension;
    }
}
