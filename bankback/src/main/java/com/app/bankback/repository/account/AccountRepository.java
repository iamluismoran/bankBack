package com.app.bankback.repository.account;

import com.app.bankback.enums.AccountStatus;
import com.app.bankback.model.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    /** Todas las cuentas donde el usuario es titular principal. */
    List<Account> findByPrimaryOwnerId(Long ownerId);

    /** Todas las cuentas donde el usuario es titular secundario. */
    List<Account> findBySecondaryOwnerId(Long ownerId);

    /** Filtrar por estado (ACTIVE / FROZEN). */
    List<Account> findByStatus(AccountStatus status);

    /**
     * Verifica acceso: ¿esta cuenta pertenece (como primary o secondary) a ownerId?
     */
    @Query(("""
           SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
           FROM Account a
           WHERE a.id = :accountId
             AND (a.primaryOwner.id = :ownerId OR a.secondaryOwner.id = :ownerId)
           """)
    boolean isOwnedBy(Long accountId, Long ownerId);
}
