package com.company.product_pricing_service.application.port.out;

import com.company.product_pricing_service.domain.Price;

import java.time.LocalDateTime;
import java.util.Optional;

public interface FindApplicablePricePort {
    /**
     * Searches the persistence source for the highest-priority applicable price.
     *
     * @param productId identifier of the requested product
     * @param brandId identifier of the requested brand
     * @param applicationDate date and time for which the price is requested
     * @return the matching price, or empty when no price applies
     */
    Optional<Price> findApplicablePrice(Long productId, Integer brandId, LocalDateTime applicationDate);
}
