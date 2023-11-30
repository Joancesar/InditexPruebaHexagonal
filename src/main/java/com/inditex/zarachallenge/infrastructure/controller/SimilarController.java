package com.inditex.zarachallenge.infrastructure.controller;

import com.inditex.zarachallenge.application.model.dto.SimilarProductDTO;
import com.inditex.zarachallenge.application.port.in.SimilarServicePort;
import com.inditex.zarachallenge.infrastructure.exception.SimilarProductsNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@RestController
public class SimilarController {

    private final SimilarServicePort similarServicePort;

    public SimilarController(SimilarServicePort similarServicePort) {
        this.similarServicePort = similarServicePort;
    }

    @GetMapping("/product/{productId}/similar")
    public ResponseEntity<List<SimilarProductDTO>> getSimilarProduct(@PathVariable long productId) {

        return Optional.of(similarServicePort.getSimilarProducts(productId))
                .filter(Predicate.not(List::isEmpty))
                .map(ResponseEntity::ok)
                .orElseThrow(() ->
                    new SimilarProductsNotFoundException("Similar Products not found with id " + productId)
                );
    }
}
