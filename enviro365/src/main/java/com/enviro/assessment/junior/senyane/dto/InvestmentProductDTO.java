package com.enviro.assessment.junior.senyane.dto;

import java.math.BigDecimal;

/**
 * Response DTO for a single investment product.
 * Used inside the portfolio response and also standalone.
 */
public class InvestmentProductDTO {

    private Long id;
    private String name;
    private String productType;
    private BigDecimal balance;

    // ---- Constructors ----

    public InvestmentProductDTO() {}

    public InvestmentProductDTO(Long id, String name, String productType, BigDecimal balance) {
        this.id = id;
        this.name = name;
        this.productType = productType;
        this.balance = balance;
    }

    // ---- Getters & Setters ----

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
}
