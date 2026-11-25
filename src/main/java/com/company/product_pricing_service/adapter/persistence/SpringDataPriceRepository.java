package com.company.product_pricing_service.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SpringDataPriceRepository extends JpaRepository<PriceEntity, Long> {
    /**
     * Finds matching prices and sorts them by descending priority.
     *
     * @param productId identifier of the requested product
     * @param brandId identifier of the requested brand
     * @param applicationDate date and time for which the price is requested
     * @param pageable limits the result to the highest-priority match
     * @return matching price records ordered from highest to lowest priority
     */
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
