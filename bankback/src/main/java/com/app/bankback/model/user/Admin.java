package com.app.bankback.model.user;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Admin: usuario administrador
 * hereda equals/hashCode de User por el id
 */

@Entity
@DiscriminatorValue("ADMIN")
@Getter @Setter
@NoArgsConstructor
public class Admin extends User{

    public Admin(String name) {
        super(name); // usa el constructor de User
    }
}
