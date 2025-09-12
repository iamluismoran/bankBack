package com.app.bankback.dto.account;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Admin: modificar directamente el balance de una cuenta concreta.
 * Útil para que Admins puedan modificar balances
 * */

@Data
public class ModifyBalanceRequest {
    private Long accountId;
    private BigDecimal newBalance;
}
