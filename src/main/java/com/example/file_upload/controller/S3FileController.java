package com.example.file_upload.controller;

// controller/S3FileController.java
import com.example.file_upload.model.entity.FileMetadata;
import com.example.file_upload.model.response.APIResponse;
import com.example.file_upload.service.S3FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.Instant;

@RestController
@RequestMapping("api/v2/files")
@RequiredArgsConstructor
public class S3FileController {

    private final S3FileService s3FileService;

    // ✅ Upload File
    @PostMapping(value = "/upload-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<APIResponse<FileMetadata>> uploadFile(
            @RequestParam MultipartFile file) {

        FileMetadata fileMetadata = s3FileService.uploadFile(file);

        APIResponse<FileMetadata> response = APIResponse.<FileMetadata>builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .message("File uploaded successfully to RustFS!")
                .payload(fileMetadata)
                .instant(Instant.now())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ✅ Preview File (View in Browser)
    @GetMapping("/preview-file/{file-name}")
    public ResponseEntity<Resource> previewFile(
            @PathVariable("file-name") String fileName) {

        Resource resource = s3FileService.getFileByFileName(fileName);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(resource);
    }

    // ✅ Download File
    @GetMapping("/download-file/{file-name}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable("file-name") String fileName) {

        Resource resource = s3FileService.getFileByFileName(fileName);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }
}