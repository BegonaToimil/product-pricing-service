package com.company.product_pricing_service.application.service;

import com.company.product_pricing_service.application.port.in.GetApplicablePriceUseCase;
import com.company.product_pricing_service.application.port.in.PriceQuery;
import com.company.product_pricing_service.application.port.out.FindApplicablePricePort;
import com.company.product_pricing_service.domain.Price;
import com.company.product_pricing_service.domain.PriceNotFoundException;

public final class PriceQueryService implements GetApplicablePriceUseCase {
    private final FindApplicablePricePort findApplicablePricePort;

    public PriceQueryService(FindApplicablePricePort findApplicablePricePort) {
        this.findApplicablePricePort = findApplicablePricePort;
    }

    @Override
    public Price getApplicablePrice(PriceQuery query) {
        return findApplicablePricePort.findApplicablePrice(
                        query.productId(),
                        query.brandId(),
                        query.applicationDate()
                )
                .orElseThrow(() -> new PriceNotFoundException(query.productId(), query.brandId()));
    }
}
