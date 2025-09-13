package com.app.bankback.model.account;

import com.app.bankback.model.user.AccountHolder;
import com.app.bankback.model.value.Money;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("CREDIT_CARD")
@Getter @Setter
@NoArgsConstructor
public class CreditCard extends Account{

    @Embedded
    @AttributeOverride(name = "amount",column = @Column(name = "credit_limit", precision = 19, scale = 2, nullable = false))
    private Money creditLimit = Money.of(100);


    /**
     * Interés anual en tanto por uno.
     * Rango permitido: [0.1, 0.2]. Default 0.2 (20%)
     */

    @Column(name = "interest_rate", precision = 4, scale = 3, nullable = false)
    private BigDecimal interestRate = new BigDecimal("0.200");

    /** Última fecha en la que se aplicaron interes mensuales*/
    @Column(name = "last_interest_date")
    private LocalDate lastInterestDate;

    // -------------------- Constructores --------------------

    public CreditCard(Money balance, AccountHolder primaryOwner) {
        super(balance, primaryOwner);
    }

    public CreditCard(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        super(balance, primaryOwner, secondaryOwner);
    }

    public CreditCard(Money balance,
                      AccountHolder primaryOwner,
                      AccountHolder secondaryOwner,
                      Money creditLimit,
                      BigDecimal interestRate) {
        super(balance, primaryOwner, secondaryOwner);
        setCreditLimit(creditLimit);
        setInterestRate(interestRate);
    }

    // -------------------- Validaciones --------------------

    public void setCreditLimit(Money creditLimit) {
        if (creditLimit == null) {
            throw new IllegalArgumentException("creditLimit cannot be null");
        }
        if (creditLimit.lt(Money.of(100))) {
            throw new IllegalArgumentException("creditLimit must be >= 100");
        }
        if (creditLimit.getAmount().compareTo(new BigDecimal("100000")) > 0) {
            throw new IllegalArgumentException("creditLimit must be <= 100000");
        }
        this.creditLimit = creditLimit;
    }

    public void setInterestRate(BigDecimal interestRate) {
        if (interestRate == null) {
            throw new IllegalArgumentException("interestRate cannot be null");
        }
        if (interestRate.compareTo(new BigDecimal("0.100")) < 0) {
            throw new IllegalArgumentException("interestRate must be >= 0.1");
        }
        if (interestRate.compareTo(new BigDecimal("0.200")) > 0) {
            throw new IllegalArgumentException("interestRate must be <= 0.2");
        }
        this.interestRate = interestRate;
    }
}
