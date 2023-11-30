package com.inditex.zarachallenge.application;

import com.inditex.zarachallenge.application.model.dto.SimilarProductDTO;
import com.inditex.zarachallenge.application.port.out.ProductRepositoryPort;
import com.inditex.zarachallenge.application.port.out.SimilarClientPort;
import com.inditex.zarachallenge.application.service.SimilarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SimilarServiceTest {
    @Mock
    private SimilarClientPort similarClientPort;
    @Mock
    private ProductRepositoryPort productRepositoryPort;
    @InjectMocks
    private SimilarService similarService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(similarService, "validFrom", "validFrom");
    }


    @Test
    void whenGetSimilarProductsThenReturnList() {
        when(similarClientPort.getSimilarProducts(anyLong())).thenReturn(List.of(1,2,3));
        when(productRepositoryPort.getSimilarProducts(anyList(), anyString()))
                .thenReturn(List.of(createSimilarProductDto()));

        List<SimilarProductDTO> similarProductDtos = similarService.getSimilarProducts(1235L);

        verify(similarClientPort).getSimilarProducts(1235L);
        verify(productRepositoryPort).getSimilarProducts(List.of(1,2,3), "validFrom");
        assertNotNull(similarProductDtos);
        assertFalse(similarProductDtos.isEmpty());
        assertNotNull(similarProductDtos.get(0));
    }

    @Test
    void whenGetSimilarProductsIsEmptyThenReturnEmptyList() {
        when(similarClientPort.getSimilarProducts(anyLong())).thenReturn(List.of());

        List<SimilarProductDTO> similarProductDtos = similarService.getSimilarProducts(1235L);

        verify(similarClientPort).getSimilarProducts(1235L);
        verifyNoInteractions(productRepositoryPort);
        assertNotNull(similarProductDtos);
        assertTrue(similarProductDtos.isEmpty());
    }


    private SimilarProductDTO createSimilarProductDto() {
        return new SimilarProductDTO() {
            @Override
            public String getId() {
                return null;
            }

            @Override
            public String getName() {
                return null;
            }

            @Override
            public BigDecimal getPrice() {
                return null;
            }

            @Override
            public boolean getAvailability() {
                return false;
            }
        };
    }
}
