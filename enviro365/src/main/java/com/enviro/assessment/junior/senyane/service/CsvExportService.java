package com.enviro.assessment.junior.senyane.service;

import com.enviro.assessment.junior.senyane.model.WithdrawalNotice;
import com.enviro.assessment.junior.senyane.repository.WithdrawalNoticeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Builds a CSV-formatted string from an investor's withdrawal history.
 * Supports optional date range filtering.
 *
 * The CSV is generated as a plain String so the controller can write it
 * directly to the HTTP response — no temp files needed.
 */
@Service
public class CsvExportService {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final WithdrawalNoticeRepository withdrawalNoticeRepository;

    public CsvExportService(WithdrawalNoticeRepository withdrawalNoticeRepository) {
        this.withdrawalNoticeRepository = withdrawalNoticeRepository;
    }

    /**
     * Generates a CSV of withdrawal notices for a given investor.
     * If fromDate and toDate are provided, only notices within that range are included.
     *
     * @param investorId the investor's ID
     * @param from       optional start of date range (inclusive)
     * @param to         optional end of date range (inclusive)
     * @return CSV content as a String
     */
    @Transactional(readOnly = true)
    public String generateCsv(Long investorId, LocalDateTime from, LocalDateTime to) {

        List<WithdrawalNotice> notices;

        // Apply date filter if both dates are provided
        if (from != null && to != null) {
            notices = withdrawalNoticeRepository
                    .findByProductPortfolioInvestorIdAndCreatedAtBetween(investorId, from, to);
        } else {
            notices = withdrawalNoticeRepository
                    .findByProductPortfolioInvestorId(investorId);
        }

        return buildCsvContent(notices);
    }

    // ---- Private helpers ----

    private String buildCsvContent(List<WithdrawalNotice> notices) {
        StringBuilder csv = new StringBuilder();

        // Header row
        csv.append("Notice ID,Product ID,Product Name,Product Type,Amount Withdrawn,Status,Note,Date\n");

        // Data rows
        for (WithdrawalNotice notice : notices) {
            csv.append(notice.getId()).append(",");
            csv.append(notice.getProduct().getId()).append(",");
            csv.append(escapeCsvField(notice.getProduct().getName())).append(",");
            csv.append(notice.getProduct().getProductType().name()).append(",");
            csv.append(notice.getAmount()).append(",");
            csv.append(notice.getStatus().name()).append(",");
            csv.append(escapeCsvField(notice.getNote())).append(",");
            csv.append(notice.getCreatedAt().format(FORMATTER)).append("\n");
        }

        return csv.toString();
    }

    /**
     * Wraps a field in quotes if it contains a comma or quote character.
     * Prevents CSV injection and malformed files.
     */
    private String escapeCsvField(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
