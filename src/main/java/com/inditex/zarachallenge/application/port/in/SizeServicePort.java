package com.inditex.zarachallenge.application.port.in;

import com.inditex.zarachallenge.application.model.event.ProductAvailabilityEvent;

public interface SizeServicePort {
    int updateSizeAvailability(ProductAvailabilityEvent productAvailabilityEvent);

}
