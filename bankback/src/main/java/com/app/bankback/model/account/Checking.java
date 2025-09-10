package com.app.bankback.model.account;


import com.app.bankback.model.user.AccountHolder;
import com.app.bankback.model.value.Money;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("CHECKING")
@Getter @Setter
@NoArgsConstructor
public class Checking extends Account{

    /** Clave secreta de la cuenta para operaciones de terceros */

    @Column(name = "secret_key", nullable = false, length = 20)
    private String secretKey;

    // -------------------- Reglas propias --------------------

    /** Saldo mínimo requerido (default 250) */

    @Embedded
    @AttributeOverride(name = "amount",
            column = @Column(name = "min_balance_amount", precision = 19, scale = 2, nullable = false))
    private Money minimumBalance = Money.of(250);

    /** Comisión de mantenimiento mensual (default 12) */

    @Embedded
    @AttributeOverride(name = "amount",
            column = @Column(name = "monthly_fee_amount", precision = 19, scale = 2, nullable = false))
    private Money monthlyMaintenanceFee = Money.of(12);

    // -------------------- Constructores --------------------

    public Checking(Money balance, AccountHolder primaryOwner, String secretKey) {
        super(balance, primaryOwner);
        this.secretKey = secretKey;
    }

    public Checking(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey) {
        super(balance, primaryOwner, secondaryOwner);
        this.secretKey = secretKey;
    }
}
