package com.inditex.zarachallenge.infrastructure.exception;

public class SimilarProductsNotFoundException extends RuntimeException {
    public SimilarProductsNotFoundException(String message) {
        super(message);
    }
}
