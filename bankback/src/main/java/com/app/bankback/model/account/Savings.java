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
@DiscriminatorValue("SAVINGS")
@Getter @Setter
@NoArgsConstructor
public class Savings extends Account{

    @Column(name = "secret_key", nullable = false, length = 20)
    private String secretKey;

    /** Mínimo de saldo requerido*/

    @Embedded
    @AttributeOverride(name = "amount",
            column = @Column(name = "min_balance_amount", precision = 19, scale = 2, nullable = false))
    private Money minimumBalance = Money.of(1000);

    /** Interés anual en tanto por uno, ej: 0.0025 = 0.25% anual */

    @Column(name = "interest_rate", precision = 5, scale = 4, nullable = false)
    private BigDecimal interestRate = new BigDecimal("0.0025");

    /** Última fecha en la que se aplicaron intereses anuales*/
    @Column(name = "last_interest_date")
    private LocalDate lastInterestDate;

    // -------------------- Constructores --------------------

    public Savings(Money balance, AccountHolder primaryOwner, String secretKey) {
        super(balance, primaryOwner);
        this.secretKey = secretKey;
    }

    public Savings(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey) {
        super(balance, primaryOwner,secondaryOwner);
        this.secretKey = secretKey;
    }

    public Savings(Money balance,
                   AccountHolder primaryOwner,
                   AccountHolder secondaryOwner,
                   String secretKey,
                   Money minimumBalance,
                   BigDecimal interestRate) {
        super(balance, primaryOwner,secondaryOwner);
        this.secretKey = secretKey;
        setMinimumBalance(minimumBalance);
        setInterestRate(interestRate);
    }

    // -------------------- Validaciones simples --------------------

    public void setMinimumBalance(Money minimumBalance) {
        if (minimumBalance == null) {
            throw new IllegalArgumentException("minimumBalance cannot be null");
        }
        if (minimumBalance.lt(Money.of(100))) {
            throw new IllegalArgumentException("minimumBalance must be >= 100");
        }
        this.minimumBalance = minimumBalance;
    }

    public void setInterestRate(BigDecimal interestRate) {
        if (interestRate == null) {
            throw new IllegalArgumentException("interestRate cannot be null");
        }
        // Enunciado: máx 0.5. (se usa > 0 para evitar valores negativos o cero)
        if (interestRate.signum() < 0) {
            throw new IllegalArgumentException("interestRate must be > 0");
        }
        if (interestRate.compareTo(new BigDecimal("0.5")) > 0) {
            throw new IllegalArgumentException("interestRate must be <= 0.5");
        }
        this.interestRate = interestRate;
    }
}
