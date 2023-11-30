package com.inditex.zarachallenge.application.port.out;

import java.sql.Timestamp;

public interface SizeRepositoryPort {
    int updateSizeAvailability(long sizeId, boolean availability, Timestamp update);
}
