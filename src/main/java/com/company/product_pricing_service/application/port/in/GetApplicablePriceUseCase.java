package com.company.product_pricing_service.application.port.in;

import com.company.product_pricing_service.domain.Price;

public interface GetApplicablePriceUseCase {
    Price getApplicablePrice(PriceQuery query);
}
