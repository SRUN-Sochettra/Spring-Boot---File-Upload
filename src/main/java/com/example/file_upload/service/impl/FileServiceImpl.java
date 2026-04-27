package com.example.file_upload.service.impl;

import com.example.file_upload.model.entity.FileMetadata;
import com.example.file_upload.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    @Value("${spring.file-upload-path}")
    private String pathName;

    @Override
    @SneakyThrows
    public FileMetadata fileUpload(MultipartFile file) throws IOException {

        Path rootPath = Paths.get(pathName);

        String fileName = file.getOriginalFilename();

        if (!Files.exists(rootPath)) {
            Files.createDirectories(rootPath);
        }

        fileName = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(fileName);

        Files.copy(file.getInputStream(), rootPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

        String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/files/preview-file/" + fileName)
                .toUriString();

        return FileMetadata.builder()
                .fileName(fileName)
                .fileUrl(fileUrl)
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .build();
    }

    @Override
    public Resource getFileByFileName(String fileName) throws IOException {
        Path rootPath = Paths.get(pathName);
        return new InputStreamResource(Files.newInputStream(rootPath.resolve(fileName)));
    }

}