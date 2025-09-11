package com.app.bankback.service.impl;

import com.app.bankback.enums.AccountStatus;
import com.app.bankback.model.account.Account;
import com.app.bankback.model.account.Checking;
import com.app.bankback.model.account.CreditCard;
import com.app.bankback.model.account.Savings;
import com.app.bankback.model.value.Money;
import com.app.bankback.repository.account.AccountRepository;
import com.app.bankback.service.interfaces.AccountOperationsService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountOperationsServiceImpl implements AccountOperationsService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional(readOnly = true)
    public Money getBalance(Long accountId) {
        Account acc = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found: " + accountId));

        applyAccruedInterestIfNeeded(acc);
        return acc.getBalance();
    }

    @Override
    public void transfer(Long fromAccountId, Long toAccountId, Money amount, Long requestOwnerId) {
        if (amount == null || amount.getAmount() == null || amount.getAmount().signum() <= 0) {
            throw new IllegalArgumentException("Amount must be > 0");
        }

        Account from = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new EntityNotFoundException("Source account not found: " + fromAccountId));
        Account to = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new EntityNotFoundException("Target account not found: " + toAccountId));

        if (from.getStatus() == AccountStatus.FROZEN || to.getStatus() == AccountStatus.FROZEN) {
            throw new IllegalArgumentException("One of the accounts is FROZEN");
        }

        applyAccruedInterestIfNeeded(from);
        applyAccruedInterestIfNeeded(to);

        if (from.getBalance().lt(amount)) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        from.setBalance(from.getBalance().subtract(amount).scaled());
        to.setBalance(to.getBalance().add(amount).scaled());

        applyPenaltyIfBelowMinimun(from);

        accountRepository.save(from);
        accountRepository.save(to);
    }

    // -------------------- Helpers --------------------


    private void applyAccruedInterestIfNeeded(Account acc) {
        if (acc instanceof Savings s) {
            LocalDate last = (s.getLastInterestDate() != null) ? s.getLastInteresDate() : s.getCreationDate();
            long years = ChronoUnit.YEARS.between(last, LocalDate.now());
            if (years > 0) {
                BigDecimal rate = s.getInterestRate();
                BigDecimal balance = s.getBalance().getAmount();
                for (int i = 0; i < years; i++) {
                    balance = balance.multiply(BigDecimal.ONE.add(rate)).setScale(2, RoundingMode.HALF_EVEN);
                }
                s.getBalance().setAmount(balance);
                s.setLastInterestDate(last.plusYears(years));
                accountRepository.save(s);
            }
        } else if (acc instanceof CreditCard c) {
            LocalDate last = (c.getLastInterestDate() != null) ? c.getLastInterestDate() : c.getCreationDate();
            long months = ChronoUnit.MONTHS.between(last, LocalDate.now());
            if (months > 0) {
                BigDecimal monthly = c.getInterestRate().divide(new BigDecimal("12"), 10, RoundingMode.HALF_EVEN);
                BigDecimal balance = c.getBalance().getAmount();
                for (int i = 0; i < months; i++) {
                    balance = balance.multiply(BigDecimal.ONE.add(monthly)).setScale(2, RoundingMode.HALF_EVEN);
                }
                c.getBalance().setAmount(balance);
                c.setLastInterestDate(last.plusMonths(months));
                accountRepository.save(c);
            }
        }
    }

    /** Penalty si (Checking/Savings) cae bajo el mínimo tras la operación */
    private void applyPenaltyIfBelowMinimum(Account acc) {
        if (acc instanceof Checking ch) {
            if (ch.getBalance().lt(ch.getMinimumBalance())) {
                ch.setBalance(ch.getBalance().subtract(acc.getPenaltyFree()).scaled());
            }
        } else if (acc instanceof Savings s) {
            if (s.getBalance().lt(s.getMinimumBalance())) {
                s.setBalance(s.getBalance().subtract(acc.getPenaltyFree()).scaled());
            }
        }
    }
}
