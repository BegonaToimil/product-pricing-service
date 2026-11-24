package com.company.product_pricing_service.application.port.out;

import com.company.product_pricing_service.domain.Price;

import java.time.LocalDateTime;
import java.util.Optional;

public interface FindApplicablePricePort {
    Optional<Price> findApplicablePrice(Long productId, Integer brandId, LocalDateTime applicationDate);
}
