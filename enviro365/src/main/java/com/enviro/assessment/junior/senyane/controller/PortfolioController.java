package com.enviro.assessment.junior.senyane.controller;

import com.enviro.assessment.junior.senyane.dto.ApiResponseDTO;
import com.enviro.assessment.junior.senyane.dto.PortfolioDTO;
import com.enviro.assessment.junior.senyane.service.PortfolioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for portfolio-related endpoints.
 *
 * Base path: /api/portfolios
 */
@RestController
@RequestMapping("/api/portfolios")
@CrossOrigin(origins = "*")  // allows the HTML frontend to call this API
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    /**
     * GET /api/portfolios
     * Returns all investor portfolios.
     * Used to populate the investor selector on the UI.
     */
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<PortfolioDTO>>> getAllPortfolios() {
        List<PortfolioDTO> portfolios = portfolioService.getAllPortfolios();
        return ResponseEntity.ok(
                ApiResponseDTO.ok("Portfolios retrieved successfully", portfolios)
        );
    }

    /**
     * GET /api/portfolios/investor/{investorId}
     * Returns the portfolio for a specific investor including all products.
     */
    @GetMapping("/investor/{investorId}")
    public ResponseEntity<ApiResponseDTO<PortfolioDTO>> getPortfolioByInvestor(
            @PathVariable Long investorId) {

        PortfolioDTO portfolio = portfolioService.getPortfolioByInvestorId(investorId);
        return ResponseEntity.ok(
                ApiResponseDTO.ok("Portfolio retrieved successfully", portfolio)
        );
    }
}
