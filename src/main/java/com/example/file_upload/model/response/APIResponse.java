package com.example.file_upload.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class APIResponse<T> {
    private Boolean success;
    private String message;
    private HttpStatus status;
    private T payload;
    private Instant instant;
}