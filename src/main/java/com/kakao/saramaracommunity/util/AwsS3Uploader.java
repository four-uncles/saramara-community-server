package com.kakao.saramaracommunity.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.kakao.saramaracommunity.bucket.exception.BucketBusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static com.kakao.saramaracommunity.bucket.exception.BucketErrorCode.BUCKET_AWS_S3_UPLOAD_FAILED;
import static com.kakao.saramaracommunity.bucket.exception.BucketErrorCode.BUCKET_IMAGE_FILE_NOT_FOUND;

/**
 * AWS S3 버킷에 파일 업로드 로직을 수행할 클래스입니다.
 * 예외 발생시 BucketBusinessException을 발생시킵니다.
 * 추후 단위 테스트 코드를 작성할 예정입니다.
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class AwsS3Uploader {

    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String upload(MultipartFile file) {
        if(checkImageFileisEmpty(file)) {
            log.info("[AwsS3Uploader] 업로드할 파일이 존재하지 않습니다.");
            throw new BucketBusinessException(BUCKET_IMAGE_FILE_NOT_FOUND);
        }
        // 파일 명 생성하기
        String fileName = FileUtil.buildFileName(file.getOriginalFilename());
        log.info("[AwsS3Uploader] 업로드할 파일명: {}", fileName);
        // 파일 형식 설정
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        // 파일의 내용을 읽어서 S3에 전송
        try (InputStream inputStream = file.getInputStream()) {
            log.info("[AwsS3Uploader] S3 버킷에 파일 업로드를 진행합니다.");
            amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            log.error("[AwsS3Uploader] S3 버킷에 파일을 업로드 하던 중 문제가 발생했습니다. 오류 정보 : {}", e.getMessage());
            throw new BucketBusinessException(BUCKET_AWS_S3_UPLOAD_FAILED);
        }
        // S3 버킷에 업로드 된 이미지 파일의 실제 URL을 리턴
        String imagePath = amazonS3Client.getUrl(bucketName, fileName).toString();
        log.info("[AwsS3Uploader] S3 파일 업로드 성공했습니다, imgpath: {}", imagePath);
        return imagePath;
    }

    private boolean checkImageFileisEmpty(MultipartFile file) {
        return file.isEmpty();
    }

}
