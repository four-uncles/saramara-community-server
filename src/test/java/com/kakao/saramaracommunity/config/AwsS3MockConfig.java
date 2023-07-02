package com.kakao.saramaracommunity.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.findify.s3mock.S3Mock;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

/**
 * AwsS3MockConfig: AWS S3 버킷 테스트를 위한 설정 클래스
 * 테스트 환경에서만 동작하도록 @Profile 어노테이션을 구성했습니다.
 *
 * @author Taejun
 * @version 0.0.1
 */
@Profile("test")
@TestConfiguration
public class AwsS3MockConfig {
    @Bean
    public S3Mock s3Mock() {
        return new S3Mock.Builder().withPort(8001).withInMemoryBackend().build();
    }
    @Primary
    @Bean(name = "amazonS3", destroyMethod = "shutdown")
    public AmazonS3Client amazonS3(){
        AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder.EndpointConfiguration("http://localhost:8001", Regions.AP_NORTHEAST_2.name());
        AmazonS3 client = AmazonS3ClientBuilder
                .standard()
                .withPathStyleAccessEnabled(true)
                .withEndpointConfiguration(endpoint)
                .withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials()))
                .build();
        return (AmazonS3Client) client;
    }

}
