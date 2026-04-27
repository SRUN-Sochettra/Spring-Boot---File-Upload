package com.example.file_upload.controller;

import com.example.file_upload.model.entity.FileMetadata;
import com.example.file_upload.model.response.APIResponse;
import com.example.file_upload.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/files")
public class FileController {

    private final FileService fileService;

    @PostMapping(value = "/upload-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<APIResponse<FileMetadata>> uploadFile(@RequestParam MultipartFile file) throws IOException, URISyntaxException {
        APIResponse<FileMetadata> apiResponse = APIResponse.<FileMetadata>builder()
                .success(true)
                .message("File upload successfully!")
                .status(HttpStatus.CREATED)
                .payload(fileService.fileUpload(file))
                .instant(Instant.now())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("/preview-file/{file-name}")
    public ResponseEntity<Resource> getFileByFileName(@PathVariable("file-name") String fileName) throws IOException {
        Resource resource = fileService.getFileByFileName(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.IMAGE_PNG)
                .body(resource);
    }

    @GetMapping("/download-file/{file-name}")
    public ResponseEntity<Resource> downloadFileByFileName(@PathVariable("file-name") String fileName) throws IOException {
        Resource resource = fileService.getFileByFileName(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

}