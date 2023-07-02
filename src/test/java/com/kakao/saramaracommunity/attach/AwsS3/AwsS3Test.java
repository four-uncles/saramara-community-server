package com.kakao.saramaracommunity.attach.AwsS3;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.kakao.saramaracommunity.config.AwsS3MockConfig;
import io.findify.s3mock.S3Mock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.FileCopyUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * AwsS3Test: S3 버킷과 연동하여 이미지 객체 업로드 여부를 테스트할 클래스
 *
 * 아직 단위 테스트와 통합 테스트 구분이 명확하지 않은 상태입니다.
 *
 * @author Taejun
 * @version 0.0.1
 */
@Import(AwsS3MockConfig.class)
@ActiveProfiles("test")
@SpringBootTest
public class AwsS3Test {

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private static final String BUCKET_NAME = "test";

    @BeforeAll
    static void setUp(@Autowired S3Mock s3Mock, @Autowired AmazonS3 amazonS3) {
        s3Mock.start();
        amazonS3.createBucket(BUCKET_NAME);
    }

    @AfterAll
    static void tearDown(@Autowired S3Mock s3Mock, @Autowired AmazonS3 amazonS3) {
        amazonS3.shutdown();
        s3Mock.stop();
    }

    @DisplayName("AWS S3 버킷에 이미지 파일을 업로드한다.")
    @Test
    void test() throws IOException {
        // given
        String path = "test.png";
        String contentType = "image/png";
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, path, new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8)), objectMetadata);
        amazonS3.putObject(putObjectRequest);

        // when
        S3Object s3Object = amazonS3.getObject(BUCKET_NAME, path);

        // then
        assertThat(s3Object.getKey()).isEqualTo(path);
        assertThat(s3Object.getObjectMetadata().getContentType()).isEqualTo(contentType);
        assertThat(new String(FileCopyUtils.copyToByteArray(s3Object.getObjectContent()))).isEqualTo("");
    }

}
