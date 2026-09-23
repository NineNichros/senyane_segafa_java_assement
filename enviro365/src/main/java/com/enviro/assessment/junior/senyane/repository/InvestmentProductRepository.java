package com.enviro.assessment.junior.senyane.repository;

import com.enviro.assessment.junior.senyane.model.InvestmentProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Data access layer for InvestmentProduct entities.
 */
@Repository
public interface InvestmentProductRepository extends JpaRepository<InvestmentProduct, Long> {

    // Retrieve all products belonging to a specific portfolio
    List<InvestmentProduct> findByPortfolioId(Long portfolioId);
}
