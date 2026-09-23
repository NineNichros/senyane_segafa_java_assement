package com.enviro.assessment.junior.senyane.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an investor's portfolio.
 * A portfolio belongs to one investor and contains multiple investment products.
 */
@Entity
@Table(name = "portfolios")
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // Each portfolio belongs to one investor
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "investor_id", nullable = false)
    private Investor investor;

    // A portfolio holds many investment products
    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<InvestmentProduct> products = new ArrayList<>();

    // ---- Constructors ----

    public Portfolio() {}

    public Portfolio(String name, Investor investor) {
        this.name = name;
        this.investor = investor;
    }

    // ---- Getters & Setters ----

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Investor getInvestor() { return investor; }
    public void setInvestor(Investor investor) { this.investor = investor; }

    public List<InvestmentProduct> getProducts() { return products; }
    public void setProducts(List<InvestmentProduct> products) { this.products = products; }
}
