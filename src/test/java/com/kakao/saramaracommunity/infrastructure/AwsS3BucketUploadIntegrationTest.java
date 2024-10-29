package com.kakao.saramaracommunity.infrastructure;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.kakao.saramaracommunity.support.IntegrationTestSupport;
import io.findify.s3mock.S3Mock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * AWS S3 버킷과 연동하여 이미지 객체 업로드 여부를 테스트할 클래스입니다.
 * 통합 테스트를 위해 @SpringBootTest를 설정했습니다.
 * 추후, 통합 테스트가 아닌 Mockking을 통한 단위 테스트로 수정할 예정입니다.
 */
class AwsS3BucketUploadIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private AmazonS3Client amazonS3Client;

    @Autowired
    private static final String BUCKET_NAME = "saramara-storage";

    @BeforeAll
    static void setUp(@Autowired S3Mock s3Mock, @Autowired AmazonS3Client amazonS3Client) {
        s3Mock.start();
        amazonS3Client.createBucket(BUCKET_NAME);
    }

    @AfterAll
    static void tearDown(@Autowired S3Mock s3Mock, @Autowired AmazonS3Client amazonS3Client) {
        amazonS3Client.shutdown();
        s3Mock.stop();
    }

    @DisplayName("AWS S3 버킷에 이미지 파일을 업로드한다.")
    @Test
    void uploadImageToS3Bucket() throws IOException {
        // given
        String path = "test.png";
        String contentType = "image/png";
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, path, new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8)), objectMetadata);
        amazonS3Client.putObject(putObjectRequest);

        // when
        S3Object s3Object = amazonS3Client.getObject(BUCKET_NAME, path);

        // then
        assertThat(s3Object.getKey()).isEqualTo(path);
        assertThat(s3Object.getObjectMetadata().getContentType()).isEqualTo(contentType);
        assertThat(new String(FileCopyUtils.copyToByteArray(s3Object.getObjectContent()))).isEmpty();
    }

}
