package com.example.file_upload.service;

// service/S3FileService.java
import com.example.file_upload.model.entity.FileMetadata;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface S3FileService {
    FileMetadata uploadFile(MultipartFile file);
    Resource getFileByFileName(String fileName);
}
