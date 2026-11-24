package com.company.product_pricing_service.adapter.config;

import com.company.product_pricing_service.application.port.in.GetApplicablePriceUseCase;
import com.company.product_pricing_service.application.port.out.FindApplicablePricePort;
import com.company.product_pricing_service.application.service.PriceQueryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfiguration {
    @Bean
    public GetApplicablePriceUseCase getApplicablePriceUseCase(FindApplicablePricePort findApplicablePricePort) {
        return new PriceQueryService(findApplicablePricePort);
    }
}
