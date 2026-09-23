package com.enviro.assessment.junior.senyane.service;

import com.enviro.assessment.junior.senyane.dto.WithdrawalRequestDTO;
import com.enviro.assessment.junior.senyane.dto.WithdrawalResponseDTO;
import com.enviro.assessment.junior.senyane.model.InvestmentProduct;
import com.enviro.assessment.junior.senyane.model.Investor;
import com.enviro.assessment.junior.senyane.model.WithdrawalNotice;
import com.enviro.assessment.junior.senyane.model.WithdrawalNotice.Status;
import com.enviro.assessment.junior.senyane.repository.InvestmentProductRepository;
import com.enviro.assessment.junior.senyane.repository.InvestorRepository;
import com.enviro.assessment.junior.senyane.repository.WithdrawalNoticeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles all withdrawal notice logic including business rule enforcement.
 *
 * Business Rules:
 *  1. Retirement products can only be withdrawn by investors older than 65.
 *  2. Withdrawal amount must not exceed the product's current balance.
 *  3. Withdrawal amount must not exceed 90% of the product's current balance.
 */
@Service
public class WithdrawalService {

    // Minimum age for RETIREMENT product withdrawals
    private static final int RETIREMENT_AGE_LIMIT = 65;

    // Maximum percentage of balance that can be withdrawn at once
    private static final BigDecimal MAX_WITHDRAWAL_PERCENTAGE = new BigDecimal("0.90");

    private final WithdrawalNoticeRepository withdrawalNoticeRepository;
    private final InvestmentProductRepository productRepository;
    private final InvestorRepository investorRepository;

    public WithdrawalService(WithdrawalNoticeRepository withdrawalNoticeRepository,
                             InvestmentProductRepository productRepository,
                             InvestorRepository investorRepository) {
        this.withdrawalNoticeRepository = withdrawalNoticeRepository;
        this.productRepository = productRepository;
        this.investorRepository = investorRepository;
    }

    /**
     * Processes a withdrawal request against a specific investment product.
     * All three business rules are validated before any balance change occurs.
     *
     * @param request the withdrawal request DTO
     * @return WithdrawalResponseDTO with the result and updated balance
     */
    @Transactional
    public WithdrawalResponseDTO processWithdrawal(WithdrawalRequestDTO request) {

        // --- Load investor ---
        Investor investor = investorRepository.findById(request.getInvestorId())
                .orElseThrow(() -> new RuntimeException("Investor not found with ID: " + request.getInvestorId()));

        // --- Load product ---
        InvestmentProduct product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + request.getProductId()));

        BigDecimal amount = request.getAmount();
        BigDecimal currentBalance = product.getBalance();

        // --- Business Rule 1: RETIREMENT age check ---
        if (product.getProductType() == InvestmentProduct.ProductType.RETIREMENT) {
            if (investor.getAge() <= RETIREMENT_AGE_LIMIT) {
                throw new RuntimeException(
                        "Retirement withdrawals are only allowed for investors older than "
                        + RETIREMENT_AGE_LIMIT + ". Investor age: " + investor.getAge()
                );
            }
        }

        // --- Business Rule 2: Amount must not exceed balance ---
        if (amount.compareTo(currentBalance) > 0) {
            throw new RuntimeException(
                    "Withdrawal amount R" + amount +
                    " exceeds available balance R" + currentBalance
            );
        }

        // --- Business Rule 3: Amount must not exceed 90% of balance ---
        BigDecimal maxAllowed = currentBalance.multiply(MAX_WITHDRAWAL_PERCENTAGE)
                                              .setScale(2, RoundingMode.HALF_DOWN);
        if (amount.compareTo(maxAllowed) > 0) {
            throw new RuntimeException(
                    "Withdrawal amount R" + amount +
                    " exceeds the maximum allowed 90% of balance (R" + maxAllowed + ")"
            );
        }

        // --- All rules passed: deduct balance and save notice ---
        BigDecimal newBalance = currentBalance.subtract(amount);
        product.setBalance(newBalance);
        productRepository.save(product);

        WithdrawalNotice notice = new WithdrawalNotice(
                amount,
                Status.APPROVED,
                "Withdrawal approved successfully",
                product
        );
        withdrawalNoticeRepository.save(notice);

        return mapToResponseDTO(notice, product, newBalance);
    }

    /**
     * Returns the full withdrawal history for an investor.
     *
     * @param investorId the investor's ID
     * @return list of all withdrawal notices for that investor
     */
    @Transactional(readOnly = true)
    public List<WithdrawalResponseDTO> getWithdrawalHistory(Long investorId) {
        return withdrawalNoticeRepository.findByProductPortfolioInvestorId(investorId)
                .stream()
                .map(notice -> mapToResponseDTO(
                        notice,
                        notice.getProduct(),
                        notice.getProduct().getBalance()
                ))
                .collect(Collectors.toList());
    }

    // ---- Private mapping helper ----

    private WithdrawalResponseDTO mapToResponseDTO(WithdrawalNotice notice,
                                                    InvestmentProduct product,
                                                    BigDecimal balanceAfter) {
        return new WithdrawalResponseDTO(
                notice.getId(),
                product.getId(),
                product.getName(),
                product.getProductType().name(),
                notice.getAmount(),
                balanceAfter,
                notice.getStatus().name(),
                notice.getNote(),
                notice.getCreatedAt()
        );
    }
}
