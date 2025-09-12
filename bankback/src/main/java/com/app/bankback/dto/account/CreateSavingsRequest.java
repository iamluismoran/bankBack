package com.app.bankback.dto.account;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Admin: crear una cuenta en Savings
 * Si minimumBalance o interestRate vienen null, se usarán defaults del service
 */
@Data
public class CreateSavingsRequest {
    private Long primaryOwnerId;
    private Long secondaryOwnerId;
    private BigDecimal initialAmount;
    private String secretKey;

    /** Mínimo primitido: >= 100. Si null -> default 1000 */
    private BigDecimal minimumBalance;

    /** Rango: 0.0 < rate <= 0.5. Si null -> default 0.0025 (0.25%)</=> */
    private BigDecimal interestRate;
}
