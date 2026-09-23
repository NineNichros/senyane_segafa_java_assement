package com.enviro.assessment.junior.senyane.exception;

import com.enviro.assessment.junior.senyane.dto.ApiResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Centralised error handling for the entire application.
 *
 * @RestControllerAdvice intercepts exceptions thrown by any controller
 * and converts them into consistent JSON responses using ApiResponseDTO.
 *
 * This means controllers stay clean — no try/catch blocks needed there.
 *
 * Handles:
 *  - RuntimeException        → 400 Bad Request (business rule violations)
 *  - MethodArgumentNotValidException → 400 Bad Request (input validation failures)
 *  - Exception               → 500 Internal Server Error (unexpected errors)
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles business rule violations thrown as RuntimeException in services.
     *
     * Examples:
     *  - "Retirement withdrawals only allowed for investors older than 65"
     *  - "Withdrawal amount exceeds 90% of balance"
     *  - "Investor not found with ID: 5"
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseDTO.error(ex.getMessage()));
    }

    /**
     * Handles @Valid annotation failures on request DTOs.
     *
     * When a field fails validation (e.g. null investorId, negative amount),
     * Spring throws MethodArgumentNotValidException. We collect all field
     * errors and return them in the response so the UI can display them.
     *
     * Response example:
     * {
     *   "success": false,
     *   "message": "Validation failed",
     *   "data": {
     *     "amount": "Withdrawal amount must be greater than zero",
     *     "investorId": "Investor ID is required"
     *   }
     * }
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<Map<String, String>>> handleValidationException(
            MethodArgumentNotValidException ex) {

        // Collect all field-level validation error messages
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ApiResponseDTO<Map<String, String>> response = new ApiResponseDTO<>(
                false,
                "Validation failed",
                errors
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    /**
     * Catch-all for any unexpected exceptions not handled above.
     * Returns a generic 500 error — avoids leaking internal stack traces.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleGenericException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponseDTO.error("An unexpected error occurred. Please try again later."));
    }
}
