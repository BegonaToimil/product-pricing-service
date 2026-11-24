package com.company.product_pricing_service.application.service;

import com.company.product_pricing_service.application.port.in.PriceQuery;
import com.company.product_pricing_service.application.port.out.FindApplicablePricePort;
import com.company.product_pricing_service.domain.Price;
import com.company.product_pricing_service.domain.PriceNotFoundException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PriceQueryServiceTest {
    private final FindApplicablePricePort pricePort = mock(FindApplicablePricePort.class);
    private final PriceQueryService service = new PriceQueryService(pricePort);

    @Test
    void returnsPriceFoundByPort() {
        final LocalDateTime date = LocalDateTime.parse("2020-06-14T16:00:00");
        final PriceQuery query = new PriceQuery(35455L, 1, date);
        final Price expected = new Price(35455L, 1, 2, 1, date, date.plusHours(2),
                new BigDecimal("25.45"), "EUR");
        when(pricePort.findApplicablePrice(35455L, 1, date)).thenReturn(Optional.of(expected));

        assertEquals(expected, service.getApplicablePrice(query));
        verify(pricePort).findApplicablePrice(35455L, 1, date);
    }

    @Test
    void throwsWhenNoApplicablePriceExists() {
        final LocalDateTime date = LocalDateTime.parse("2020-06-16T21:00:00");
        final PriceQuery query = new PriceQuery(35455L, 1, date);
        when(pricePort.findApplicablePrice(35455L, 1, date)).thenReturn(Optional.empty());

        assertThrows(PriceNotFoundException.class, () -> service.getApplicablePrice(query));
    }
}
