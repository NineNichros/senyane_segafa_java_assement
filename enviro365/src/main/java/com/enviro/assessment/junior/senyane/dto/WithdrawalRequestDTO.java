package com.enviro.assessment.junior.senyane.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Request DTO for submitting a withdrawal notice.
 * Validated before reaching the service layer.
 */
public class WithdrawalRequestDTO {

    @NotNull(message = "Investor ID is required")
    private Long investorId;

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Withdrawal amount is required")
    @DecimalMin(value = "0.01", message = "Withdrawal amount must be greater than zero")
    private BigDecimal amount;

    // ---- Constructors ----

    public WithdrawalRequestDTO() {}

    public WithdrawalRequestDTO(Long investorId, Long productId, BigDecimal amount) {
        this.investorId = investorId;
        this.productId = productId;
        this.amount = amount;
    }

    // ---- Getters & Setters ----

    public Long getInvestorId() { return investorId; }
    public void setInvestorId(Long investorId) { this.investorId = investorId; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
