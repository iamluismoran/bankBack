package com.app.bankback.service.interfaces;

import com.app.bankback.model.value.Money;

public interface ThirdPartyService {

    /** Abono de un tercero: exige hashedKey (header), secretKey de la cuenta y amount. */
    void thirdPartyDeposit(String hashedKey, Long accountId, String accountSecretKey, Money amount);

    /** Cargo de un tercero: exige hashedKey (header), secretKey de la cuenta y amount. */
    void thirdPartyWithdraw(String hashedKey, Long accountId, String accountSecretKey, Money amount);
}
