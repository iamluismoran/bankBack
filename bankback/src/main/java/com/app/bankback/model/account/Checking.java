package com.app.bankback.model.account;


import com.app.bankback.model.user.AccountHolder;
import com.app.bankback.model.value.Money;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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

    //  Fecha de última comisión cobrada
    @Column(name = "last_monthly_fee_date")
    private LocalDate lastMonthlyFeeDate;
    //

    // -------------------- Constructores --------------------

    public Checking(Money balance, AccountHolder primaryOwner, String secretKey) {
        super(balance, primaryOwner);
        this.secretKey = secretKey;
    }

    public Checking(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey) {
        super(balance, primaryOwner, secondaryOwner);
        this.secretKey = secretKey;
    }

    // -------------------- Lógica de comisiones/penalización --------------------
    /**
     * Cobra tantas comisiones mensuales como hayan pasado desde la última vez
     * y aplica penalización si el saldo queda por debajo del mínimo.
     * Llamar desde el service antes de devolver/usar el balance.
     */

    public void applyMonthlyUpdates(LocalDate today) {
        if (today == null) today = LocalDate.now();

        // Si nunca se cobró comisión, arrancamos desde la fecha de creación
        if (lastMonthlyFeeDate == null) {
            lastMonthlyFeeDate = getCreationDate();
        }

        long months = ChronoUnit.MONTHS.between(lastMonthlyFeeDate, today);
        if (months <= 0) return;

        // totalFee = monthlyMaintenanceFee * months
        BigDecimal totalFee = monthlyMaintenanceFee.getAmount()
                .multiply(BigDecimal.valueOf(months));
        // balance = balance - totalFee
        setBalance(Money.of(getBalance().getAmount().subtract(totalFee)));

        // avanzar marca de tiempo
        lastMonthlyFeeDate = lastMonthlyFeeDate.plusMonths(months);

        // Si baja del mínimo, penalización
        if (getBalance().lt(minimumBalance)) {
            setBalance(Money.of(getBalance().getAmount().subtract(getPenaltyFee().getAmount())));
        }
    }
}
