package com.inditex.zarachallenge.infrastructure.persistence;

import com.inditex.zarachallenge.application.port.out.SizeRepositoryPort;
import com.inditex.zarachallenge.domain.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.Optional;

public interface SizeRepository extends SizeRepositoryPort, JpaRepository<Size, Long> {

    @Modifying
    @Query(value =
            "UPDATE SIZE s " +
            "SET s.AVAILABILITY = :availability, " +
            "s.LAST_UPDATED= :update " +
            "WHERE s.SIZE_ID = :sizeId ",
            nativeQuery = true)
    int updateSizeAvailability(long sizeId, boolean availability, Timestamp update);

}
