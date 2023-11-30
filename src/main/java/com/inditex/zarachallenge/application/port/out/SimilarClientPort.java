package com.inditex.zarachallenge.application.port.out;

import java.util.List;

public interface SimilarClientPort {
    List<Integer> getSimilarProducts(long productId);
}
