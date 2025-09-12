package com.app.bankback.dto.account;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Holder: transferir dinero entre cuentas.
 * El service valida que requesterOwnerId sea propietario de fromAccountId.
 */
@Data
public class TransferRequest {
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
    private Long requestOwnerId;
}
