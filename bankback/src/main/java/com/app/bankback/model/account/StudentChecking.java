package com.app.bankback.model.account;

import com.app.bankback.model.user.AccountHolder;
import com.app.bankback.model.value.Money;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("STUDENT_CHECKING")
@Getter @Setter
@NoArgsConstructor
public class StudentChecking extends Account{

    /** Clave secreta de la cuenta para operaciones de terceros */

    @Column(name = "secret_key", nullable = false, length = 20)
    private String secretKey;

    // -------------------- Constructores --------------------

    public StudentChecking(Money balance, AccountHolder primaryOwner, String secretKey) {
        super(balance, primaryOwner);
        this.secretKey = secretKey;
    }

    public StudentChecking(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey) {
        super(balance, primaryOwner, secondaryOwner);
        this.secretKey = secretKey;
    }
}
