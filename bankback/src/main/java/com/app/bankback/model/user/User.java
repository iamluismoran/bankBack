package com.app.bankback.model.user;

import jakarta.persistence.*;
import lombok.*;

/**
 * Clase base para ADMIN, ACCOUNT_HOLDER Y THIRD_PARTY
 */

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role",length = 20)
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @Column(nullable = false, length = 50)
    @ToString.Include
    private String name;

    /**
     * Constructor pensado para subclases Admin, AccountHolder y ThirdParty.
     * protected: lo usan las subclases y el paquete, pero no el resto de la app.
     */
    protected User(String name) {
        this.name = name;
    }
}
