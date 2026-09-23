package com.enviro.assessment.junior.senyane.controller;

import com.enviro.assessment.junior.senyane.service.CsvExportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * REST controller for CSV export of withdrawal statements.
 *
 * Base path: /api/export
 */
@RestController
@RequestMapping("/api/export")
@CrossOrigin(origins = "*")
public class CsvExportController {

    private final CsvExportService csvExportService;

    public CsvExportController(CsvExportService csvExportService) {
        this.csvExportService = csvExportService;
    }

    /**
     * GET /api/export/withdrawals/{investorId}
     * Downloads a CSV file of withdrawal statements for the investor.
     *
     * Optional query parameters for date filtering:
     *   from=2024-01-01T00:00:00
     *   to=2024-12-31T23:59:59
     *
     * Example: /api/export/withdrawals/1?from=2024-01-01T00:00:00&to=2024-12-31T23:59:59
     *
     * The response uses Content-Disposition: attachment so the browser
     * triggers a file download automatically.
     */
    @GetMapping("/withdrawals/{investorId}")
    public ResponseEntity<byte[]> exportWithdrawals(
            @PathVariable Long investorId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {

        String csvContent = csvExportService.generateCsv(investorId, from, to);

        String filename = "withdrawals_investor_" + investorId + ".csv";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvContent.getBytes());
    }
}
