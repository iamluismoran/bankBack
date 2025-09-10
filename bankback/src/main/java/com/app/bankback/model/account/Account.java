package com.app.bankback.model.account;

import com.app.bankback.enums.AccountStatus;
import com.app.bankback.model.user.AccountHolder;
import com.app.bankback.model.value.Money;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Account (base) para Cheking, StudentChecking, Savings y CreditCard
 */

@Entity
@Table(name = "accounts")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type", length = 20)
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(onlyExplicitlyIncluded = true)
public abstract class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Long id;

    // -------------------- Dinero y comisiones --------------------

    /**
     * Saldo de la cuenta
     * @AttributeOverride: renombramos la columna para evitar choques con otros Money embebidos.
     */
    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "balance_amount", precision = 19, scale = 2, nullable = false))
    private Money balance;

    /**
     * Penalización fija cuando se viola el mínimo
     */
    @Embedded
    @AttributeOverride(name = "amount",
    column = @Column(name = "penalty_free_amount", precision = 19, scale = 2, nullable = false))
    private Money penaltyFree = Money.of(40);

    // -------------------- Propietarios --------------------

    /**
     * Titular principal
     * LAZY: asi no cargamos al dueño hasta que se necesite
     */

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "primary_owner_id", nullable = false)
    private AccountHolder primaryOwner;

    /** Titular secundario */

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "secondary_owner_id")
    private AccountHolder secondaryOwner;

    // -------------------- Metadatos --------------------

    /**
     * Fecha de creación de la cuenta, por defecto hoy
     * Se puede setear  al crear la cuenta si asi se quiere
     */

    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    private AccountStatus status = AccountStatus.ACTIVE;

    // -------------------- Constructores de conveniencia --------------------

    /** Crea cuenta con saldo y dueño principal */

    protected Account(Money balance, AccountHolder primaryOwner) {
        this.balance = balance;
        this.primaryOwner = primaryOwner;
    }

    /** Crea cuenta con saldo, dueño principal y secundario */

    protected Account(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
    }
}
