package com.app.bankback.repository.user;

import com.app.bankback.model.user.ThirdParty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ThirdPartyRepository extends JpaRepository<ThirdParty, Long> {

    Optional<ThirdParty> findByHashedKey(String hashedKey);

    boolean existsByHashedKey(String hashedKey);
}
