package com.inditex.zarachallenge.application.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

public interface SimilarProductDTO {
    String getId();
    String getName();
    BigDecimal getPrice();
    boolean getAvailability();
}
