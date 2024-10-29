package com.kakao.saramaracommunity.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

//@Configuration
@Profile("!test")
public class MinioConfig {

//    @Value("${minio.endpoint}")
    private String endPoint;

//    @Value("${minio.access_key}")
    private String accessKey;

//    @Value("${minio.secret_key}")
    private String secretKey;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endPoint)
                .credentials(accessKey, secretKey)
                .build();
    }

}
