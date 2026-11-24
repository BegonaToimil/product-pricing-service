package com.company.product_pricing_service.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PriceTest {
    private static final LocalDateTime START = LocalDateTime.parse("2020-06-14T00:00:00");
    private static final LocalDateTime END = LocalDateTime.parse("2020-12-31T23:59:59");

    @Test
    void acceptsValidPrice() {
        assertDoesNotThrow(() -> new Price(35455L, 1, 1, 0, START, END,
                new BigDecimal("35.50"), "EUR"));
    }

    @Test
    void rejectsInvalidPriceInterval() {
        assertThrows(IllegalArgumentException.class, () -> new Price(
                35455L, 1, 1, 0, END, START, new BigDecimal("35.50"), "EUR"
        ));
    }

    @Test
    void rejectsNegativePrice() {
        assertThrows(IllegalArgumentException.class, () -> new Price(
                35455L, 1, 1, 0, START, END, new BigDecimal("-0.01"), "EUR"
        ));
    }

    @Test
    void rejectsUnknownCurrency() {
        assertThrows(IllegalArgumentException.class, () -> new Price(
                35455L, 1, 1, 0, START, END, new BigDecimal("35.50"), "EURO"
        ));
    }
}
