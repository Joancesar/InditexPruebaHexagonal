package com.inditex.zarachallenge.infrastructure.persistence;

import com.inditex.zarachallenge.application.model.dto.SimilarProductDTO;
import com.inditex.zarachallenge.application.port.out.ProductRepositoryPort;
import com.inditex.zarachallenge.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends ProductRepositoryPort, JpaRepository<Product, Long> {

    @Query(value =
            "SELECT p.id, p.name, o.price, s.availability " +
            "FROM PRODUCT p " +
            "INNER JOIN OFFER o " +
            "ON p.id = o.PRODUCT_ID " +
            "AND o.VALID_FROM = ( " +
            "   SELECT MAX(o2.VALID_FROM) " +
            "   FROM OFFER o2 " +
            "   WHERE o2.PRODUCT_ID = p.id " +
            "   AND o2.VALID_FROM <= :validFrom " +
            ") " +
            "INNER JOIN SIZE s " +
            "ON p.id = s.PRODUCT_ID " +
            "WHERE p.id IN (:ids) " +
            "ORDER BY s.availability ",
            nativeQuery = true)
    List<SimilarProductDTO> getSimilarProducts(List<Integer> ids, String validFrom);
}
