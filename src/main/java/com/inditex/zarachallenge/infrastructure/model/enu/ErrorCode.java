package com.inditex.zarachallenge.infrastructure.model.enu;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

    RESOURCE_NOT_FOUND("Resource not found with requested criteria",  HttpStatus.NOT_FOUND),
    INTERNAL_SERVER_ERROR("Unexpected Error", HttpStatus.INTERNAL_SERVER_ERROR),
    UNPROCESSABLE_REQUEST("Unprocessable request", HttpStatus.UNPROCESSABLE_ENTITY);

    private final String message;
    private final HttpStatus httpStatus;
}