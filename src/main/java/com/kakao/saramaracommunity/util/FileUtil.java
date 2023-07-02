package com.kakao.saramaracommunity.util;

/**
 * FileUtil: 파일의 originalFileName(원본이름)을 인자로 받아 파일명을 새로 지어주는 클래스
 *
 * @author Taejun
 * @version 0.0.1
 */
public class FileUtil {

    private static final String FILE_EXTENSION_SEPARATOR = ".";

    private static final String CATEGORY_PREFIX = "/";

    private static final String TIME_SEPARATOR = "_";

    private static final int UNDER_BAR_INDEX = 1;

    public static String buildFileName(String originalFileName) {
        int fileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        String fileExtension = originalFileName.substring(fileExtensionIndex);
        String fileName = originalFileName.substring(0, fileExtensionIndex);
        String now = String.valueOf(System.currentTimeMillis());
        return CATEGORY_PREFIX + fileName + TIME_SEPARATOR + now + fileExtension;
    }

}
