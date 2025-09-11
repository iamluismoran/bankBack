package com.app.bankback.service.interfaces;

import com.app.bankback.model.value.Money;

public interface AccountOperationsService {

    /** Devuelve el balance aplicando intereses si corresponde. */
    Money getBalance(Long accountId);

    /** Transferencia entre cuentas (valida ownership, fondos, penalty, etc.). */
    void transfer(Long fromAccountId,
                  Long toAccountId,
                  Money amount,
                  Long requesterOwnerId);
}
