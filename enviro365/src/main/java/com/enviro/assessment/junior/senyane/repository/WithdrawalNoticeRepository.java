package com.enviro.assessment.junior.senyane.repository;

import com.enviro.assessment.junior.senyane.model.WithdrawalNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data access layer for WithdrawalNotice entities.
 */
@Repository
public interface WithdrawalNoticeRepository extends JpaRepository<WithdrawalNotice, Long> {

    // Get all withdrawal notices for a specific product
    List<WithdrawalNotice> findByProductId(Long productId);

    // Get all withdrawal notices for a specific investor's product
    // Used for withdrawal history and CSV export
    List<WithdrawalNotice> findByProductPortfolioInvestorId(Long investorId);

    // Filter withdrawal history by date range — used for CSV export filtering
    List<WithdrawalNotice> findByProductPortfolioInvestorIdAndCreatedAtBetween(
            Long investorId,
            LocalDateTime from,
            LocalDateTime to
    );
}
