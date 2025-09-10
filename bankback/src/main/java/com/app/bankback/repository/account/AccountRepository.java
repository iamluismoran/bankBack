package com.app.bankback.repository.account;

import com.app.bankback.model.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    /** Todas las cuentas donde el usuario es titular principal. */
    List<Account> findByPrimaryOwnerId(Long ownerId);

    /** Todas las cuentas donde el usuario es titular secundario. */
    List<Account> findBySecondaryOwnerId(Long ownerId);

    /** Filtrar por estado (ACTIVE / FROZEN). */
    List<Account> findByStatus(Long status);

    /**
     * Verifica acceso: ¿esta cuenta pertenece (como primary o secondary) a ownerId?
     * Lo usamos para autorizar lecturas/transferencias de un AccountHolder.
     */
    @Query("""
           SELECT a
           FROM Account a WHERE a.id = :accountId
           AND (a.primaryOwner.id = :ownerId OR a.secondaryOwner.id = :ownerId)
           """)
    Optional<Account> findAccessibleByOwner(Long accountId, Long ownerId);
}
