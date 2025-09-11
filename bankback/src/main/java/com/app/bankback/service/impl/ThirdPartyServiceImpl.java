package com.app.bankback.service.impl;

import com.app.bankback.enums.AccountStatus;
import com.app.bankback.model.account.*;
import com.app.bankback.model.value.Money;
import com.app.bankback.repository.account.AccountRepository;
import com.app.bankback.repository.user.ThirdPartyRepository;
import com.app.bankback.service.interfaces.ThirdPartyService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ThirdPartyServiceImpl implements ThirdPartyService {

    private final ThirdPartyRepository thirdPartyRepository;
    private final AccountRepository accountRepository;

    @Override
    public void thirdPartyDeposit(String hashedKey, Long accountId, String accountSecretKey, Money amount) {
        validateThirdParty(hashedKey);
        Account acc = loadAccountForSecretKey(accountId, accountSecretKey);

        if (acc.getStatus() == AccountStatus.FROZEN) {
            throw new IllegalArgumentException("Account is FROZEN");
        }
        if (amount == null || amount.getAmount() == null || amount.getAmount().signum() <= 0) {
            throw new IllegalArgumentException("Amount must be > 0");
        }

        acc.setBalance(acc.getBalance().add(amount).scaled());
        accountRepository.save(acc);
    }

    @Override
    public void thirdPartyWithdraw(String hashedKey, Long accountId, String accountSecretKey, Money amount) {
        validateThirdParty(hashedKey);
        Account acc = loadAccountForSecretKey(accountId, accountSecretKey);

        if (acc.getStatus() == AccountStatus.FROZEN) {
            throw new IllegalArgumentException("Account is FROZEN");
        }
        if (amount == null || amount.getAmount() == null || amount.getAmount().signum() <= 0) {
            throw new IllegalArgumentException("Amount must be > 0");
        }
        if (acc.getBalance().lt(amount)) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        acc.setBalance(acc.getBalance().subtract(amount).scaled());
        applyPenaltyIfBelowMinimum(acc);
        accountRepository.save(acc);
    }

    // -------------------- Helpers --------------------

    /** No devuelve nada: valida existencia y lanza excepción si no existe */
    private void validateThirdParty(String hashedKey) {
        if (!thirdPartyRepository.existsByHashedKey(hashedKey)) {
            throw new SecurityException("Invalid third-party hashed key");
        }
    }

    /** Carga cuenta y valida secretKey (Checking, StudentChecking, Savings). CreditCard no lo soporta */
    private Account loadAccountForSecretKey(Long accountId, String providedSecret) {
        Account acc = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found: " + accountId));

        String realSecret = extractSecretKeyOrThrow(acc);

        if (!realSecret.equals(providedSecret)) {
            throw new SecurityException("Invalid account secret key");
        }
        return acc;
    }

    /** Extrae el secretKey según el subtipo o lanza la excepción adecuada */
    private String extractSecretKeyOrThrow(Account acc) {
        if (acc instanceof Checking c) {
            return c.getSecretKey();
        }
        if (acc instanceof StudentChecking s) {
            return s.getSecretKey();
        }
        if (acc instanceof Savings sv) {
            return sv.getSecretKey();
        }
        if (acc instanceof CreditCard) {
            throw new IllegalArgumentException("CreditCard accounts do not support secretKey-based operations");
        }
        throw new IllegalArgumentException("Unsupported account type: " + acc.getClass().getSimpleName());
    }

    /** Cobra penaltyFee si Checking/Savings queda bajo el mínimo. */
    private void applyPenaltyIfBelowMinimum(Account acc) {
        if (acc instanceof Checking ch) {
            if (ch.getBalance().lt(ch.getMinimumBalance())) {
                ch.setBalance(ch.getBalance().subtract(acc.getPenaltyFee()).scaled());
            }
        } else if (acc instanceof Savings s) {
            if (s.getBalance().lt(s.getMinimumBalance())) {
                s.setBalance(s.getBalance().subtract(acc.getPenaltyFee()).scaled());
            }
        }
    }
}
