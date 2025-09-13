package com.app.bankback.model.user;

import com.app.bankback.model.value.Address;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * AccounHolder: Cliente del banco
 * hereda equals/hashCode de User por el id
 */

@Entity
@DiscriminatorValue("ACCOUNT_HOLDER")
@Getter @Setter
@NoArgsConstructor
public class AccountHolder extends User {

    @Column(name = "date_of_birth", nullable = true)
    private LocalDate dateOfBirth;

    // === Dirección principal (requerida a nivel negocio, nullable en DB por SINGLE_TABLE) ===
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street",     column = @Column(name = "street",      length = 120, nullable = true)),
            @AttributeOverride(name = "city",       column = @Column(name = "city",        length = 80,  nullable = true)),
            @AttributeOverride(name = "country",    column = @Column(name = "country",     length = 30,  nullable = true)),
            @AttributeOverride(name = "postalCode", column = @Column(name = "postal_code", length = 16,  nullable = true))
    })
    private Address primaryAddress;

    // === Dirección de envío (opcional) ===
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street",     column = @Column(name = "mail_street",      length = 120, nullable = true)),
            @AttributeOverride(name = "city",       column = @Column(name = "mail_city",        length = 80,  nullable = true)),
            @AttributeOverride(name = "country",    column = @Column(name = "mail_country",     length = 30,  nullable = true)),
            @AttributeOverride(name = "postalCode", column = @Column(name = "mail_postal_code", length = 16,  nullable = true))
    })
    private Address mailingAddress; // puede quedar en null

    // Constructor de conveniencia para crear objetos claros
    public AccountHolder(String name,LocalDate dateOfBirth, Address primaryAddress) {
        super(name);
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
    }

    public AccountHolder(String name, LocalDate dateOfBirth, Address primaryAddress, Address mailingAddress) {
        super(name);
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
        this.mailingAddress = mailingAddress; // opcional
    }
}
