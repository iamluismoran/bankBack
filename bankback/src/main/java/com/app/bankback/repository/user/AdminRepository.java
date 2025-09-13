package com.app.bankback.repository.user;

import com.app.bankback.model.user.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByNameIgnoreCase(String name);
}
