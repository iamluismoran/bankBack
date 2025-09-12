package com.app.bankback.dto.account;

import lombok.Data;

import java.math.BigDecimal;
/**
 * Admin: crear una cuenta de tarjeta de crédito.
 * Si creditLimit o interestRate vienen null, se usarán defaults del service.
 */
@Data
public class CreateCreditCardRequest {
    private Long primaryOwnerId;
    private Long secondaryOwnerId;
    private BigDecimal initialAmount;

    /** Límite de crédito: (100, 1000000). Si  null -> default 100 */
    private BigDecimal creditLimit;

    /** Interés anual: (0.1, 0.2). Si null -> default 0.2 */
    private BigDecimal interestRate;
}
