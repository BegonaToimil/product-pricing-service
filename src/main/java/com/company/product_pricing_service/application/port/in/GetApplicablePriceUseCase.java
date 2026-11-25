package com.company.product_pricing_service.application.port.in;

import com.company.product_pricing_service.domain.Price;

public interface GetApplicablePriceUseCase {
    /**
     * Finds the applicable price for a product, brand, and application date.
     *
     * @param query price lookup parameters
     * @return the applicable price
     * @throws com.company.product_pricing_service.domain.PriceNotFoundException
     *         if no price matches
     */
    Price getApplicablePrice(PriceQuery query);
}
