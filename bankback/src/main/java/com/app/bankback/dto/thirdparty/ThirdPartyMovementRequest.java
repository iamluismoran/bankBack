package com.app.bankback.dto.thirdparty;

import lombok.Data;

import java.math.BigDecimal;


/**
 * Terceros: depósito o retiro.
 * El hashedKey del tercero se envía en el HEADER.
 * Aquí viaja el secretKey de la cuenta y el importe.
 */
@Data
public class ThirdPartyMovementRequest {
    private Long accountId;
    private String accountSecretKey;
    private BigDecimal amount;
}
