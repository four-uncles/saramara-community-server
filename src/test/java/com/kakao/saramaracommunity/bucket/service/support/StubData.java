package com.kakao.saramaracommunity.bucket.service.support;

import lombok.Getter;

@Getter
public class StubData {

    @Getter
    public static class CustomMultipartFile {

        @Getter
        static final String IMAGE_URL = "https://saramara-storage.s3.ap-northeast-2.amazonaws.com/test.png";

    }
}
