package com.company.product_pricing_service.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SpringDataPriceRepository extends JpaRepository<PriceEntity, Long> {
    @Query("""
            select price from PriceEntity price
            where price.productId = :productId
              and price.brandId = :brandId
              and price.startDate <= :applicationDate
              and price.endDate >= :applicationDate
            order by price.priority desc
            """)
    List<PriceEntity> findApplicablePrices(
            @Param("productId") Long productId,
            @Param("brandId") Integer brandId,
            @Param("applicationDate") LocalDateTime applicationDate,
            Pageable pageable
    );
}
