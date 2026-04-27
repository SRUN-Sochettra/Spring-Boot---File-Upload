package com.example.file_upload.service.impl;

// service/impl/S3FileServiceImpl.java
import com.example.file_upload.model.entity.FileMetadata;
import com.example.file_upload.service.S3FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3FileServiceImpl implements S3FileService {

    private final S3Client s3Client;

    @Value("${rustfs.bucket.name}")
    private String bucketName;

    // ✅ Auto-create bucket if not exists
    private void createBucketIfNotExists() {
        try {
            s3Client.headBucket(HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build());
        } catch (NoSuchBucketException e) {
            s3Client.createBucket(CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build());
        }
    }

    @Override
    public FileMetadata uploadFile(MultipartFile file) {
        createBucketIfNotExists();

        // ✅ Generate unique file name
        String originalFileName = file.getOriginalFilename();
        String extension = StringUtils.getFilenameExtension(originalFileName);
        String fileName = UUID.randomUUID() + "." + extension;

        // ✅ Fallback content type
        String contentType = file.getContentType() != null
                ? file.getContentType()
                : "application/octet-stream";

        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileName)
                            .contentType(contentType)
                            .build(),
                    RequestBody.fromInputStream(
                            file.getInputStream(),
                            file.getSize()
                    )
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to RustFS", e);
        }

        // ✅ Build preview URL
        String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v2/files/preview-file/" + fileName)
                .toUriString();

        return FileMetadata.builder()
                .fileName(fileName)
                .fileUrl(fileUrl)
                .fileType(contentType)
                .fileSize(file.getSize())
                .build();
    }

    @Override
    public Resource getFileByFileName(String key) {
        try {
            InputStream inputStream = s3Client.getObject(
                    GetObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .build()
            );
            return new InputStreamResource(inputStream);

        } catch (NoSuchKeyException e) {
            throw new RuntimeException("File not found: " + key, e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve file from RustFS", e);
        }
    }
}