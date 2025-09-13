package com.app.bankback.repository.user;

import com.app.bankback.model.user.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountHolderRepository extends JpaRepository<AccountHolder, Long> {

    /** Búsqueda exacta ignorando mayúsculas y minúsculas */
    Optional<AccountHolder> findByNameIgnoreCase(String name);

    /** Búsqueda parcial por nombre */
    List<AccountHolder> findByNameContainingIgnoreCase(String fragment);
}
