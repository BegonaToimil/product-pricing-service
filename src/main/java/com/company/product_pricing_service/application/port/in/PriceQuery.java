package com.company.product_pricing_service.application.port.in;

import java.time.LocalDateTime;
import java.util.Objects;

public record PriceQuery(Long productId, Integer brandId, LocalDateTime applicationDate) {
    public PriceQuery {
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("Product ID must be positive");
        }
        if (brandId == null || brandId <= 0) {
            throw new IllegalArgumentException("Brand ID must be positive");
        }
        Objects.requireNonNull(applicationDate, "Application date is required");
    }
}
