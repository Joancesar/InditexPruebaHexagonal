package com.inditex.zarachallenge.infrastructure.exception;

public class SimilarProductsUnexpectedException extends RuntimeException {
    public SimilarProductsUnexpectedException(String message) {
        super(message);
    }
}
