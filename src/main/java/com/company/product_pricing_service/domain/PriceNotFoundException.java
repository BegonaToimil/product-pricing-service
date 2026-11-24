package com.company.product_pricing_service.domain;

import java.io.Serial;

public class PriceNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public PriceNotFoundException(Long productId, Integer brandId) {
        super("No applicable price found for product " + productId + " and brand " + brandId);
    }
}
