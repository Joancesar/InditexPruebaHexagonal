package com.inditex.zarachallenge.infrastructure.config;


import com.inditex.zarachallenge.infrastructure.exception.SimilarProductsNotFoundException;
import com.inditex.zarachallenge.infrastructure.model.enu.ErrorCode;
import com.inditex.zarachallenge.infrastructure.model.ErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.TypeMismatchException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SimilarProductsNotFoundException.class)
    public ResponseEntity<ErrorDTO> handlePriceNotFoundException(SimilarProductsNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, request.getRequestURI(), ErrorCode.RESOURCE_NOT_FOUND);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class,
            MethodArgumentNotValidException.class,
            ServletRequestBindingException.class,
            TypeMismatchException.class,
            HttpMessageConversionException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeException.class})
    public ResponseEntity<ErrorDTO> handleUnprocessableRequestException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, request.getRequestURI(), ErrorCode.UNPROCESSABLE_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleGeneralException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, request.getRequestURI(), ErrorCode.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorDTO> buildErrorResponse(Exception ex, String path, ErrorCode errorCode) {
        ErrorDTO errorDTO = new ErrorDTO(errorCode, ex.getMessage(), path);
        return new ResponseEntity<>(errorDTO, errorCode.getHttpStatus());
    }

}