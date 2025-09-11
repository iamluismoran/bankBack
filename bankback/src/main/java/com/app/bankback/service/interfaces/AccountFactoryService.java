package com.app.bankback.service.interfaces;

import com.app.bankback.model.account.Account;
import com.app.bankback.model.account.CreditCard;
import com.app.bankback.model.account.Savings;
import com.app.bankback.model.value.Money;

import java.math.BigDecimal;

public interface AccountFactoryService {

    Account createCheckingOrStudent(Long primaryOwnerId,
                                    Long secondaryOwnerId,
                                    Money initialBalance,
                                    String secretKey);

    Savings createSavings(Long primaryOwnerId,
                          Long secondaryOwnerId,
                          Money initialBalance,
                          String secretKey,
                          Money minimumBalance,
                          BigDecimal interestRate);

    CreditCard createCreditCard(Long primaryOwnerId,
                                Long secondaryOwnerId,
                                Money initialBalance,
                                Money creditLimit,
                                BigDecimal interestRate);
}
