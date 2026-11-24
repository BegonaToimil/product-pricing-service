package com.company.product_pricing_service.adapter.web;

import com.company.product_pricing_service.application.port.in.GetApplicablePriceUseCase;
import com.company.product_pricing_service.application.port.in.PriceQuery;
import com.company.product_pricing_service.domain.Price;
import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Validated
@RestController
@RequestMapping("/api/v1/prices")
public class PriceController {
    private static final Logger log = LoggerFactory.getLogger(PriceController.class);

    private final GetApplicablePriceUseCase getApplicablePriceUseCase;

    public PriceController(GetApplicablePriceUseCase getApplicablePriceUseCase) {
        this.getApplicablePriceUseCase = getApplicablePriceUseCase;
    }

    @GetMapping
    public PriceResponse getApplicablePrice(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate,
            @RequestParam @Positive Long productId,
            @RequestParam @Positive Integer brandId
    ) {
        log.info("Looking up price for product {} and brand {} at {}", productId, brandId, applicationDate);
        final PriceQuery query = new PriceQuery(productId, brandId, applicationDate);
        final Price price = getApplicablePriceUseCase.getApplicablePrice(query);
        log.info("Found applicable price list {}: {} {}", price.priceList(), price.amount(), price.currency());
        return PriceResponse.from(price);
    }
}
