package com.enviro.assessment.junior.senyane.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO returned after a withdrawal notice is processed.
 * Includes all relevant details the frontend needs to display.
 */
public class WithdrawalResponseDTO {

    private Long noticeId;
    private Long productId;
    private String productName;
    private String productType;
    private BigDecimal amountWithdrawn;
    private BigDecimal balanceAfter;
    private String status;
    private String note;
    private LocalDateTime createdAt;

    // ---- Constructors ----

    public WithdrawalResponseDTO() {}

    public WithdrawalResponseDTO(Long noticeId, Long productId, String productName,
                                 String productType, BigDecimal amountWithdrawn,
                                 BigDecimal balanceAfter, String status,
                                 String note, LocalDateTime createdAt) {
        this.noticeId = noticeId;
        this.productId = productId;
        this.productName = productName;
        this.productType = productType;
        this.amountWithdrawn = amountWithdrawn;
        this.balanceAfter = balanceAfter;
        this.status = status;
        this.note = note;
        this.createdAt = createdAt;
    }

    // ---- Getters & Setters ----

    public Long getNoticeId() { return noticeId; }
    public void setNoticeId(Long noticeId) { this.noticeId = noticeId; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }

    public BigDecimal getAmountWithdrawn() { return amountWithdrawn; }
    public void setAmountWithdrawn(BigDecimal amountWithdrawn) { this.amountWithdrawn = amountWithdrawn; }

    public BigDecimal getBalanceAfter() { return balanceAfter; }
    public void setBalanceAfter(BigDecimal balanceAfter) { this.balanceAfter = balanceAfter; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
