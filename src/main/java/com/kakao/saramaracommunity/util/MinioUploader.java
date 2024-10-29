package com.kakao.saramaracommunity.util;

import com.kakao.saramaracommunity.bucket.exception.BucketBusinessException;
import com.kakao.saramaracommunity.bucket.service.port.ObjectStorageClient;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import static com.kakao.saramaracommunity.bucket.exception.BucketErrorCode.BUCKET_MINIO_BUCKET_NOT_FOUND;
import static com.kakao.saramaracommunity.bucket.exception.BucketErrorCode.BUCKET_MINIO_UPLOAD_FAILED;

//@Primary
//@Component
@RequiredArgsConstructor
public class MinioUploader implements ObjectStorageClient {

    private final MinioClient minioClient;

    @Value("${minio.bucket_name}")
    private String bucketName;

    @Override
    public String upload(MultipartFile file) {


        if(isBucketExists(minioClient)) {
            throw new BucketBusinessException(BUCKET_MINIO_BUCKET_NOT_FOUND);
        }

        return uplodObject(file, minioClient);
    }

    private boolean isBucketExists(MinioClient minioClient) {
        try {
            return minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(bucketName)
                            .build());
        } catch (Exception e) {
            throw new BucketBusinessException(BUCKET_MINIO_UPLOAD_FAILED);
        }
    }

    private String uplodObject(MultipartFile file, MinioClient minioClient) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket("saramara-storage")
                            .object(file.getOriginalFilename())
                            .build()
            );
        } catch (Exception e) {
            throw new BucketBusinessException(BUCKET_MINIO_UPLOAD_FAILED);
        }
    }

}
