package com.kakao.saramaracommunity.architecture.support;

import org.apache.commons.lang3.ArrayUtils;

public class CommonPackageProvider {

    public static String[] combineWithCommonPackages(String... packages) {
        return ArrayUtils.addAll(packages, COMMON_ALLOWED_PACKAGES);
    }

    private static final String[] COMMON_ALLOWED_PACKAGES = {
            "java..",
            "org.springframework.."
    };

}
