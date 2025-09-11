package com.app.bankback.repository.user;

import com.app.bankback.model.user.AccountHolder;
import com.app.bankback.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /** Búsqueda exacta ignorando mayúsculas y minúsculas */
    Optional<AccountHolder> findByNameIgnoreCase(String name);

    /** Búsqueda parcial por nombre */
    List<AccountHolder> findByContainingIgnoreCase(String fragment);
}
