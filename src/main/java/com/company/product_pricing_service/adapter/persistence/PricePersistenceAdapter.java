package com.company.product_pricing_service.adapter.persistence;

import com.company.product_pricing_service.application.port.out.FindApplicablePricePort;
import com.company.product_pricing_service.domain.Price;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class PricePersistenceAdapter implements FindApplicablePricePort {
    private final SpringDataPriceRepository repository;

    public PricePersistenceAdapter(SpringDataPriceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Price> findApplicablePrice(Long productId, Integer brandId, LocalDateTime applicationDate) {
        return repository.findApplicablePrices(productId, brandId, applicationDate, PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .map(entity -> new Price(
                        entity.getProductId(),
                        entity.getBrandId(),
                        entity.getPriceList(),
                        entity.getPriority(),
                        entity.getStartDate(),
                        entity.getEndDate(),
                        entity.getAmount(),
                        entity.getCurrency()
                ));
    }
}
