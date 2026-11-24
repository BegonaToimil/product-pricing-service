package com.company.product_pricing_service.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Objects;

public record Price(
        Long productId,
        Integer brandId,
        Integer priceList,
        Integer priority,
        LocalDateTime startDate,
        LocalDateTime endDate,
        BigDecimal amount,
        String currency
) {
    public Price {
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("Product ID must be positive");
        }
        if (brandId == null || brandId <= 0) {
            throw new IllegalArgumentException("Brand ID must be positive");
        }
        if (priceList == null || priceList <= 0) {
            throw new IllegalArgumentException("Price list ID must be positive");
        }
        Objects.requireNonNull(priority, "Price priority is required");
        Objects.requireNonNull(startDate, "Start date is required");
        Objects.requireNonNull(endDate, "End date is required");
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Price start date must not be after end date");
        }
        Objects.requireNonNull(amount, "Price amount is required");
        if (amount.signum() < 0) {
            throw new IllegalArgumentException("Price amount must not be negative");
        }
        try {
            Currency.getInstance(Objects.requireNonNull(currency, "Currency is required"));
        } catch (final IllegalArgumentException exception) {
            throw new IllegalArgumentException("Currency must be a valid ISO 4217 code", exception);
        }
    }
}
