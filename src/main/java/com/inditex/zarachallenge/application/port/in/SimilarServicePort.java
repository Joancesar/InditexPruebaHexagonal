package com.inditex.zarachallenge.application.port.in;

import com.inditex.zarachallenge.application.model.dto.SimilarProductDTO;

import java.util.List;

public interface SimilarServicePort {
    List<SimilarProductDTO> getSimilarProducts(long productId);
}
