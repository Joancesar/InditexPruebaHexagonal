package com.inditex.zarachallenge.application.service;

import com.inditex.zarachallenge.application.model.dto.SimilarProductDTO;
import com.inditex.zarachallenge.application.port.in.SimilarServicePort;
import com.inditex.zarachallenge.application.port.out.ProductRepositoryPort;
import com.inditex.zarachallenge.application.port.out.SimilarClientPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class SimilarService implements SimilarServicePort {

    private final SimilarClientPort similarClientPort;
    private final ProductRepositoryPort productRepositoryPort;

    @Value("${date}")
    private String validFrom;

    public SimilarService(SimilarClientPort similarClientPort, ProductRepositoryPort productRepositoryPort) {
        this.similarClientPort = similarClientPort;
        this.productRepositoryPort = productRepositoryPort;
    }

    public List<SimilarProductDTO> getSimilarProducts(long productId) {
        List<Integer> ids = similarClientPort.getSimilarProducts(productId);
        return Optional.of(ids)
                .filter(Predicate.not(List::isEmpty))
                .map(list -> productRepositoryPort.getSimilarProducts(list, validFrom))
                .orElse(Collections.emptyList());
    }
}
