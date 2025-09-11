package com.app.bankback.repository.account;

import com.app.bankback.model.account.Checking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckingRepository extends JpaRepository<Checking, Long> {
}
