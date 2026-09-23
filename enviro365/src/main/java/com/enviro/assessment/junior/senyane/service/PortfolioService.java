package com.enviro.assessment.junior.senyane.service;

import com.enviro.assessment.junior.senyane.dto.InvestmentProductDTO;
import com.enviro.assessment.junior.senyane.dto.InvestorDTO;
import com.enviro.assessment.junior.senyane.dto.PortfolioDTO;
import com.enviro.assessment.junior.senyane.model.Investor;
import com.enviro.assessment.junior.senyane.model.Portfolio;
import com.enviro.assessment.junior.senyane.repository.PortfolioRepository;
import com.enviro.assessment.junior.senyane.repository.InvestorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles retrieval of investor portfolio data.
 * Maps entities to DTOs so the controller never touches raw JPA entities.
 */
@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final InvestorRepository investorRepository;

    public PortfolioService(PortfolioRepository portfolioRepository,
                            InvestorRepository investorRepository) {
        this.portfolioRepository = portfolioRepository;
        this.investorRepository = investorRepository;
    }

    /**
     * Retrieves the portfolio for a given investor.
     *
     * @param investorId the investor's ID
     * @return PortfolioDTO with investor details and all products
     * @throws RuntimeException if investor or portfolio is not found
     */
    @Transactional(readOnly = true)
    public PortfolioDTO getPortfolioByInvestorId(Long investorId) {

        // Verify the investor exists first
        Investor investor = investorRepository.findById(investorId)
                .orElseThrow(() -> new RuntimeException("Investor not found with ID: " + investorId));

        // Fetch their portfolio
        Portfolio portfolio = portfolioRepository.findByInvestorId(investorId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found for investor ID: " + investorId));

        return mapToDTO(portfolio, investor);
    }

    /**
     * Retrieves all investor portfolios.
     * Useful for admin or dropdown selection on the UI.
     */
    @Transactional(readOnly = true)
    public List<PortfolioDTO> getAllPortfolios() {
        return portfolioRepository.findAll()
                .stream()
                .map(portfolio -> mapToDTO(portfolio, portfolio.getInvestor()))
                .collect(Collectors.toList());
    }

    // ---- Private mapping helpers ----

    private PortfolioDTO mapToDTO(Portfolio portfolio, Investor investor) {

        // Map investor entity -> DTO
        InvestorDTO investorDTO = new InvestorDTO(
                investor.getId(),
                investor.getFirstName(),
                investor.getLastName(),
                investor.getEmail(),
                investor.getDateOfBirth(),
                investor.getAge()
        );

        // Map each product entity -> DTO
        List<InvestmentProductDTO> productDTOs = portfolio.getProducts()
                .stream()
                .map(product -> new InvestmentProductDTO(
                        product.getId(),
                        product.getName(),
                        product.getProductType().name(),
                        product.getBalance()
                ))
                .collect(Collectors.toList());

        return new PortfolioDTO(
                portfolio.getId(),
                portfolio.getName(),
                investorDTO,
                productDTOs
        );
    }
}
