package com.kakao.saramaracommunity.bucket.service.support;

import com.kakao.saramaracommunity.bucket.service.port.ObjectStorageClient;
import org.springframework.web.multipart.MultipartFile;

public class FakeMinioUploader implements ObjectStorageClient {
    @Override
    public String upload(MultipartFile file) {
        return " http://minio:9000/saramara-storage/test.png";
    }
}
