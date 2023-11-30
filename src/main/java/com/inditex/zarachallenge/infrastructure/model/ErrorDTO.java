package com.inditex.zarachallenge.infrastructure.model;

import com.inditex.zarachallenge.infrastructure.model.enu.ErrorCode;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorDTO(
        ErrorCode errorCode,
        HttpStatus status,
        int statusCode,
        String message,
        String debugMessage,
        LocalDateTime timestamp,
        String path) {

    public ErrorDTO(ErrorCode errorCode, String debugMessage, String path) {
        this(errorCode, errorCode.getHttpStatus(), errorCode.getHttpStatus().value(), errorCode.getMessage(),
                debugMessage, LocalDateTime.now(), path);
    }
}