package com.enviro.assessment.junior.senyane.repository;

import com.enviro.assessment.junior.senyane.model.Investor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Data access layer for Investor entities.
 * Spring Data JPA provides all standard CRUD operations automatically.
 */
@Repository
public interface InvestorRepository extends JpaRepository<Investor, Long> {

    // Find investor by email — used for lookups and uniqueness checks
    Optional<Investor> findByEmail(String email);
}
