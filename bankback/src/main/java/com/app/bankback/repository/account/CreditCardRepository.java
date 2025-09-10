package com.app.bankback.repository.account;

import com.app.bankback.model.account.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
}
