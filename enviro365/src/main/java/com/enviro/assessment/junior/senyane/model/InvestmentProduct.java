package com.enviro.assessment.junior.senyane.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Represents a single investment product within a portfolio.
 * Products have a type (e.g. RETIREMENT, SAVINGS) and a current balance.
 *
 * Business rule:
 *  - RETIREMENT products can only be withdrawn by investors older than 65.
 *  - Withdrawals cannot exceed 90% of the product's balance.
 */
@Entity
@Table(name = "investment_products")
public class InvestmentProduct {

    /**
     * Supported product types.
     * RETIREMENT - subject to age restriction (investor must be > 65).
     * SAVINGS    - no age restriction.
     */
    public enum ProductType {
        RETIREMENT,
        SAVINGS
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductType productType;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal balance;

    // Each product belongs to one portfolio
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    // ---- Constructors ----

    public InvestmentProduct() {}

    public InvestmentProduct(String name, ProductType productType, BigDecimal balance, Portfolio portfolio) {
        this.name = name;
        this.productType = productType;
        this.balance = balance;
        this.portfolio = portfolio;
    }

    // ---- Getters & Setters ----

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public ProductType getProductType() { return productType; }
    public void setProductType(ProductType productType) { this.productType = productType; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public Portfolio getPortfolio() { return portfolio; }
    public void setPortfolio(Portfolio portfolio) { this.portfolio = portfolio; }
}
