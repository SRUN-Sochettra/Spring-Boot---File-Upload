package com.example.file_upload.service;

import com.example.file_upload.model.entity.FileMetadata;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    FileMetadata fileUpload(MultipartFile file) throws IOException;

    Resource getFileByFileName(String fileName) throws IOException;
}