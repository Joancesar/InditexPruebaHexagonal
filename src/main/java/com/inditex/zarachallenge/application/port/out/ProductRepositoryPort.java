package com.inditex.zarachallenge.application.port.out;

import com.inditex.zarachallenge.application.model.dto.SimilarProductDTO;

import java.util.List;

public interface ProductRepositoryPort {

    List<SimilarProductDTO> getSimilarProducts(List<Integer> ids, String validFrom);
}
