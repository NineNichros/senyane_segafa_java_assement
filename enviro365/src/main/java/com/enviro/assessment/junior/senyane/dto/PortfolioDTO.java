package com.enviro.assessment.junior.senyane.dto;

import java.util.List;

/**
 * Response DTO for a full portfolio view.
 * Includes investor details and all their investment products.
 */
public class PortfolioDTO {

    private Long portfolioId;
    private String portfolioName;
    private InvestorDTO investor;
    private List<InvestmentProductDTO> products;

    // ---- Constructors ----

    public PortfolioDTO() {}

    public PortfolioDTO(Long portfolioId, String portfolioName,
                        InvestorDTO investor, List<InvestmentProductDTO> products) {
        this.portfolioId = portfolioId;
        this.portfolioName = portfolioName;
        this.investor = investor;
        this.products = products;
    }

    // ---- Getters & Setters ----

    public Long getPortfolioId() { return portfolioId; }
    public void setPortfolioId(Long portfolioId) { this.portfolioId = portfolioId; }

    public String getPortfolioName() { return portfolioName; }
    public void setPortfolioName(String portfolioName) { this.portfolioName = portfolioName; }

    public InvestorDTO getInvestor() { return investor; }
    public void setInvestor(InvestorDTO investor) { this.investor = investor; }

    public List<InvestmentProductDTO> getProducts() { return products; }
    public void setProducts(List<InvestmentProductDTO> products) { this.products = products; }
}
