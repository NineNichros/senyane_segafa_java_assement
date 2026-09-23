package com.enviro.assessment.junior.senyane.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Records a withdrawal notice made by an investor against a specific product.
 * Each notice captures the amount withdrawn and when it happened.
 *
 * Status values:
 *  PENDING   - submitted but not yet processed
 *  APPROVED  - passed all business rule validations
 *  REJECTED  - failed a business rule validation
 */
@Entity
@Table(name = "withdrawal_notices")
public class WithdrawalNotice {

    public enum Status {
        PENDING,
        APPROVED,
        REJECTED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // Optional note explaining rejection or approval detail
    private String note;

    // The product this withdrawal was made against
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private InvestmentProduct product;

    // ---- Constructors ----

    public WithdrawalNotice() {}

    public WithdrawalNotice(BigDecimal amount, Status status, String note, InvestmentProduct product) {
        this.amount = amount;
        this.status = status;
        this.note = note;
        this.product = product;
        this.createdAt = LocalDateTime.now();
    }

    // ---- Getters & Setters ----

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public InvestmentProduct getProduct() { return product; }
    public void setProduct(InvestmentProduct product) { this.product = product; }
}
