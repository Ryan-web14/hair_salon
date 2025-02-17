package com.sni.hairsalon.exception;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private String stackTrace;
    
    public static ErrorResponse of(String message, int status, String error, String path, String stackTrace) {
        return ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(status)
            .error(error)
            .message(message)
            .path(path)
            .stackTrace(stackTrace)
            .build();
    }
}