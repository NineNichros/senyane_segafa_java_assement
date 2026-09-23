package com.enviro.assessment.junior.senyane.controller;

import com.enviro.assessment.junior.senyane.dto.ApiResponseDTO;
import com.enviro.assessment.junior.senyane.dto.WithdrawalRequestDTO;
import com.enviro.assessment.junior.senyane.dto.WithdrawalResponseDTO;
import com.enviro.assessment.junior.senyane.service.WithdrawalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for withdrawal notice endpoints.
 *
 * Base path: /api/withdrawals
 */
@RestController
@RequestMapping("/api/withdrawals")
@CrossOrigin(origins = "*")
public class WithdrawalController {

    private final WithdrawalService withdrawalService;

    public WithdrawalController(WithdrawalService withdrawalService) {
        this.withdrawalService = withdrawalService;
    }

    /**
     * POST /api/withdrawals
     * Submits a new withdrawal notice.
     *
     * Request body is validated via @Valid before reaching the service.
     * Business rule violations are thrown as RuntimeExceptions and handled
     * by the GlobalExceptionHandler.
     *
     * Returns 201 CREATED on success.
     */
    @PostMapping
    public ResponseEntity<ApiResponseDTO<WithdrawalResponseDTO>> submitWithdrawal(
            @Valid @RequestBody WithdrawalRequestDTO request) {

        WithdrawalResponseDTO response = withdrawalService.processWithdrawal(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDTO.ok("Withdrawal submitted successfully", response));
    }

    /**
     * GET /api/withdrawals/history/{investorId}
     * Returns the full withdrawal history for a given investor.
     * Used to populate the withdrawal history table on the UI.
     */
    @GetMapping("/history/{investorId}")
    public ResponseEntity<ApiResponseDTO<List<WithdrawalResponseDTO>>> getHistory(
            @PathVariable Long investorId) {

        List<WithdrawalResponseDTO> history = withdrawalService.getWithdrawalHistory(investorId);
        return ResponseEntity.ok(
                ApiResponseDTO.ok("Withdrawal history retrieved successfully", history)
        );
    }
}
