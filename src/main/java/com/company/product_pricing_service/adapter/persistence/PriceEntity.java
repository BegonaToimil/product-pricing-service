package com.company.product_pricing_service.adapter.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "PRICES")
public class PriceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "BRAND_ID", nullable = false)
    private Integer brandId;

    @Column(name = "START_DATE", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "END_DATE", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "PRICE_LIST", nullable = false)
    private Integer priceList;

    @Column(name = "PRODUCT_ID", nullable = false)
    private Long productId;

    @Column(name = "PRIORITY", nullable = false)
    private Integer priority;

    @Column(name = "PRICE", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "CURR", nullable = false, length = 3)
    private String currency;

    protected PriceEntity() {
    }

    public Long getId() {
        return id;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public Integer getPriceList() {
        return priceList;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getPriority() {
        return priority;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }
}
