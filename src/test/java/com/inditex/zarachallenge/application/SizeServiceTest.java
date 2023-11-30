package com.inditex.zarachallenge.application;

import com.inditex.zarachallenge.application.model.event.ProductAvailabilityEvent;
import com.inditex.zarachallenge.application.port.out.SizeRepositoryPort;
import com.inditex.zarachallenge.application.service.SizeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SizeServiceTest {
    @Mock
    private SizeRepositoryPort sizeRepositoryPort;
    @InjectMocks
    private SizeService sizeService;

    @Test
    void whenUpdateSizeAvailabilityThenReturnOneInteger() {
        when(sizeRepositoryPort.updateSizeAvailability(anyLong(), anyBoolean(), any())).thenReturn(1);
        ProductAvailabilityEvent event = ProductAvailabilityEvent.builder().build();
        event.setAvailability(true);
        event.setUpdate(Timestamp.from(Instant.now()));
        event.setSizeId(1235L);

        int updatedRows = sizeService.updateSizeAvailability(event);

        verify(sizeRepositoryPort).updateSizeAvailability(eq(1235L), eq(true), any(Timestamp.class));
        assertEquals(1, updatedRows);
    }

    @Test
    void whenUpdateSizeAvailabilityParamsAreNullThenReturnZeroInteger() {
        ProductAvailabilityEvent event = ProductAvailabilityEvent.builder().build();

        int updatedRows = sizeService.updateSizeAvailability(event);

        verifyNoInteractions(sizeRepositoryPort);
        assertEquals(0, updatedRows);
    }

    @Test
    void whenUpdateSizeAvailabilityIsNullThenReturnZeroInteger() {

        int updatedRows = sizeService.updateSizeAvailability(null);

        verifyNoInteractions(sizeRepositoryPort);
        assertEquals(0, updatedRows);
    }
}
