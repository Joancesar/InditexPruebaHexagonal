package com.inditex.zarachallenge.domain;

import com.inditex.zarachallenge.domain.enu.SizeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
public class Size {

    @Id
    private Long sizeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "SIZE", nullable = false)
    private SizeType sizeType;

    @Column(nullable = false)
    private boolean availability;

    @Column(nullable = false)
    private Timestamp lastUpdated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;
}
