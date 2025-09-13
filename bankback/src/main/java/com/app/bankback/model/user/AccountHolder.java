package com.app.bankback.model.user;

import com.app.bankback.model.value.Address;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
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

    @Embedded
    private Address primaryAddress;

    // Constructor de conveniencia para crear objetos claros
    public AccountHolder(String name,LocalDate dateOfBirth, Address primaryAddress) {
        super(name);
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
    }
}
