package com.kakao.saramaracommunity.util;

/**
 * 파일의 originalFileName(원본명)을 인자로 받아 파일명을 새로 만드는 클래스입니다.
 */
public class FileUtil {

    private static final String FILE_EXTENSION_SEPARATOR = ".";
    private static final String CATEGORY_PREFIX = "/";
    private static final String TIME_SEPARATOR = "_";

    public static String buildFileName(String originalFileName) {
        int fileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        String fileExtension = originalFileName.substring(fileExtensionIndex);
        String fileName = originalFileName.substring(0, fileExtensionIndex);
        String now = String.valueOf(System.currentTimeMillis());
        return CATEGORY_PREFIX + fileName + TIME_SEPARATOR + now + fileExtension;
    }

}
