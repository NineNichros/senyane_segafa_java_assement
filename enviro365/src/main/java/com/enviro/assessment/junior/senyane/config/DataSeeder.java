package com.enviro.assessment.junior.senyane.config;

import com.enviro.assessment.junior.senyane.model.*;
import com.enviro.assessment.junior.senyane.model.InvestmentProduct.ProductType;
import com.enviro.assessment.junior.senyane.model.WithdrawalNotice.Status;
import com.enviro.assessment.junior.senyane.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Seeds the H2 in-memory database with test data on application startup.
 *
 * Implements CommandLineRunner so it runs automatically after the
 * application context is loaded — no manual setup needed.
 *
 * Test investors are designed to cover all business rule scenarios:
 *
 *  Investor 1 - Thabo Nkosi (age 70)
 *    - Has RETIREMENT product → withdrawal ALLOWED (age > 65)
 *    - Has SAVINGS product    → withdrawal always allowed
 *
 *  Investor 2 - Lerato Dlamini (age 45)
 *    - Has RETIREMENT product → withdrawal BLOCKED (age <= 65)
 *    - Has SAVINGS product    → withdrawal always allowed
 *
 *  Investor 3 - Sipho Mokoena (age 68)
 *    - Has SAVINGS product only → tests 90% cap rule
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final InvestorRepository investorRepository;
    private final PortfolioRepository portfolioRepository;
    private final InvestmentProductRepository productRepository;
    private final WithdrawalNoticeRepository withdrawalNoticeRepository;

    public DataSeeder(InvestorRepository investorRepository,
                      PortfolioRepository portfolioRepository,
                      InvestmentProductRepository productRepository,
                      WithdrawalNoticeRepository withdrawalNoticeRepository) {
        this.investorRepository = investorRepository;
        this.portfolioRepository = portfolioRepository;
        this.productRepository = productRepository;
        this.withdrawalNoticeRepository = withdrawalNoticeRepository;
    }

    @Override
    public void run(String... args) {
        seedInvestors();
        System.out.println("=== Enviro365: Test data seeded successfully ===");
    }

    private void seedInvestors() {

        // ----------------------------------------------------------------
        // INVESTOR 1: Thabo Nkosi — age 70 (qualifies for RETIREMENT)
        // ----------------------------------------------------------------
        Investor thabo = new Investor(
                "Thabo",
                "Nkosi",
                "thabo.nkosi@enviro365.co.za",
                LocalDate.of(1954, 3, 15)  // age ~70
        );
        investorRepository.save(thabo);

        Portfolio thaboPortfolio = new Portfolio("Thabo's Investment Portfolio", thabo);
        portfolioRepository.save(thaboPortfolio);

        InvestmentProduct thaboRetirement = new InvestmentProduct(
                "Retirement Annuity Fund",
                ProductType.RETIREMENT,
                new BigDecimal("850000.00"),
                thaboPortfolio
        );
        productRepository.save(thaboRetirement);

        InvestmentProduct thaboSavings = new InvestmentProduct(
                "Flexible Savings Plan",
                ProductType.SAVINGS,
                new BigDecimal("120000.00"),
                thaboPortfolio
        );
        productRepository.save(thaboSavings);

        // Seed some past withdrawal history for Thabo
        WithdrawalNotice thaboWithdrawal1 = new WithdrawalNotice(
                new BigDecimal("15000.00"),
                Status.APPROVED,
                "Withdrawal approved successfully",
                thaboRetirement
        );
        thaboWithdrawal1.setCreatedAt(LocalDateTime.now().minusDays(30));
        withdrawalNoticeRepository.save(thaboWithdrawal1);

        WithdrawalNotice thaboWithdrawal2 = new WithdrawalNotice(
                new BigDecimal("5000.00"),
                Status.APPROVED,
                "Withdrawal approved successfully",
                thaboSavings
        );
        thaboWithdrawal2.setCreatedAt(LocalDateTime.now().minusDays(10));
        withdrawalNoticeRepository.save(thaboWithdrawal2);

        // ----------------------------------------------------------------
        // INVESTOR 2: Lerato Dlamini — age 45 (too young for RETIREMENT)
        // ----------------------------------------------------------------
        Investor lerato = new Investor(
                "Lerato",
                "Dlamini",
                "lerato.dlamini@enviro365.co.za",
                LocalDate.of(1979, 7, 22)  // age ~45
        );
        investorRepository.save(lerato);

        Portfolio leratoPortfolio = new Portfolio("Lerato's Growth Portfolio", lerato);
        portfolioRepository.save(leratoPortfolio);

        InvestmentProduct leratoRetirement = new InvestmentProduct(
                "Pension Preservation Fund",
                ProductType.RETIREMENT,
                new BigDecimal("450000.00"),
                leratoPortfolio
        );
        productRepository.save(leratoRetirement);

        InvestmentProduct leratoSavings = new InvestmentProduct(
                "Tax-Free Savings Account",
                ProductType.SAVINGS,
                new BigDecimal("75000.00"),
                leratoPortfolio
        );
        productRepository.save(leratoSavings);

        // ----------------------------------------------------------------
        // INVESTOR 3: Sipho Mokoena — age 68 (tests 90% cap)
        // ----------------------------------------------------------------
        Investor sipho = new Investor(
                "Sipho",
                "Mokoena",
                "sipho.mokoena@enviro365.co.za",
                LocalDate.of(1956, 11, 5)  // age ~68
        );
        investorRepository.save(sipho);

        Portfolio siphoPortfolio = new Portfolio("Sipho's Balanced Portfolio", sipho);
        portfolioRepository.save(siphoPortfolio);

        InvestmentProduct siphoRetirement = new InvestmentProduct(
                "Living Annuity",
                ProductType.RETIREMENT,
                new BigDecimal("1200000.00"),
                siphoPortfolio
        );
        productRepository.save(siphoRetirement);

        InvestmentProduct siphoSavings = new InvestmentProduct(
                "Money Market Account",
                ProductType.SAVINGS,
                new BigDecimal("200000.00"),
                siphoPortfolio
        );
        productRepository.save(siphoSavings);

        WithdrawalNotice siphoWithdrawal = new WithdrawalNotice(
                new BigDecimal("50000.00"),
                Status.APPROVED,
                "Withdrawal approved successfully",
                siphoSavings
        );
        siphoWithdrawal.setCreatedAt(LocalDateTime.now().minusDays(5));
        withdrawalNoticeRepository.save(siphoWithdrawal);
    }
}
