package com.app.bankback.dto.account;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Admin: solicitar la creación de una cuenta corriente.
 * La lógica del service decidirá si es Checking o StudentChecking
 * según la edad del titular principal (< 24 → StudentChecking).
 */
@Data
public class CreateCheckingRequest {

    /** ID del titular principal (AccountHolder) */
    private Long primaryOwnerId;

    /** ID del titular secundario  */
    private Long secondaryOwnerId;

    /** Saldo inicial en EUR */
    private BigDecimal initialAmount;

    /** Clave secreta de la cuenta (para operaciones de terceros) */
    private String secretKey;
}
