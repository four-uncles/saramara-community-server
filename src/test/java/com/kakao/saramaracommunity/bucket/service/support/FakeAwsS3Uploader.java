package com.kakao.saramaracommunity.bucket.service.support;

import com.kakao.saramaracommunity.bucket.service.port.ObjectStorageClient;
import org.springframework.web.multipart.MultipartFile;

public class FakeAwsS3Uploader implements ObjectStorageClient {

    @Override
    public String upload(MultipartFile file) {
        return "https://saramara-storage.s3.ap-northeast-2.amazonaws.com/test.png";
    }

}
