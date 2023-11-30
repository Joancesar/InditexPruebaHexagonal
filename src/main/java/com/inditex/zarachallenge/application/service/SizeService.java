package com.inditex.zarachallenge.application.service;

import com.inditex.zarachallenge.application.model.event.ProductAvailabilityEvent;
import com.inditex.zarachallenge.application.port.in.SizeServicePort;
import com.inditex.zarachallenge.application.port.out.SizeRepositoryPort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class SizeService implements SizeServicePort {
    private final SizeRepositoryPort sizeRepositoryPort;

    public SizeService(SizeRepositoryPort sizeRepositoryPort) {
        this.sizeRepositoryPort = sizeRepositoryPort;
    }

    @Transactional
    public int updateSizeAvailability(ProductAvailabilityEvent productAvailabilityEvent) {
        if(productAvailabilityEvent == null || productAvailabilityEvent.getSizeId() == null ||
                productAvailabilityEvent.getUpdate() == null) {
            return 0;
        }
        return sizeRepositoryPort.updateSizeAvailability(
                productAvailabilityEvent.getSizeId(),
                productAvailabilityEvent.isAvailability(),
                productAvailabilityEvent.getUpdate()
        );
    }
}
