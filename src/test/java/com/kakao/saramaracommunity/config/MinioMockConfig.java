package com.kakao.saramaracommunity.config;

import io.minio.MinioClient;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class MinioMockConfig extends MinioConfig {

    @Bean
    @Override
    public MinioClient minioClient() {
        return Mockito.mock(MinioClient.class);
    }

}
