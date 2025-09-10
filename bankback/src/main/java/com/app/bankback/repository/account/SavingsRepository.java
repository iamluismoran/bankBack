package com.app.bankback.repository.account;

import com.app.bankback.model.account.Savings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavingsRepository extends JpaRepository<Savings, Long> {
}
