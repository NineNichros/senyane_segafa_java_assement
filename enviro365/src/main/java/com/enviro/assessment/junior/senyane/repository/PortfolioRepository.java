package com.enviro.assessment.junior.senyane.repository;

import com.enviro.assessment.junior.senyane.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Data access layer for Portfolio entities.
 */
@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    // Find a portfolio by the investor who owns it
    Optional<Portfolio> findByInvestorId(Long investorId);
}
