package com.app.bankback.model.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Embeddable
@Data
@NoArgsConstructor
public class Money {

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal amount;

    public Money(BigDecimal amount) {
        this.amount = amount;
    }

    public static Money of(double value) {
        return new Money(BigDecimal.valueOf(value));

    }

    /** Suma otro Money (devuelve NUEVO Money; no muta this) */
    public Money add(Money other) {
        return new Money(this.amount.add(other.amount));
    }

    /** Resta otro Money (devuelve NUEVO Money) */

    public Money subtract(Money other) {
        return new Money(this.amount.subtract(other.amount));
    }

    /** Multiplica por un factor (intereses/fees) */
    public Money multiply(BigDecimal factor) {
        return new Money(this.amount.multiply(factor));
    }

    /** Ajusta a 2 decimales con redondeo bancario (HALF_EVEN)
     * ("round half to even")
     */
    public Money scaled() {
        return new Money(this.amount.setScale(2, RoundingMode.HALF_EVEN));
    }

    public boolean lt(Money other) {
        return this.amount.compareTo(other.amount) < 0;
    }
}
