package com.kakao.saramaracommunity.bucket.service.port;

import org.springframework.web.multipart.MultipartFile;

public interface ObjectStorageClient {

    String upload(MultipartFile file);

}
