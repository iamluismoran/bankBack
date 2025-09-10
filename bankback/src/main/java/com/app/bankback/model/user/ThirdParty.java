package com.app.bankback.model.user;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ThirdParty: Usuario externo que opera por API con hashedKey
 * hereda equals/hashCode de User por el id
 */

@Entity
@DiscriminatorValue("THIRD_PARTY")
@Getter @Setter
@NoArgsConstructor
public class ThirdParty extends User{

    @Column(name = "hashed_key", nullable = false, unique = true, length = 64)
    private String hashedKey;

    public ThirdParty(String name, String hashedKey) {
        super(name);
        this.hashedKey = hashedKey;
    }
}
