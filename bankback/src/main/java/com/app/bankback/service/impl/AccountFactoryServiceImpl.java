package com.app.bankback.service.impl;


import com.app.bankback.model.account.*;
import com.app.bankback.model.user.AccountHolder;
import com.app.bankback.model.value.Money;
import com.app.bankback.repository.account.AccountRepository;
import com.app.bankback.repository.user.AccountHolderRepository;
import com.app.bankback.service.interfaces.AccountFactoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountFactoryServiceImpl implements AccountFactoryService {

    private final AccountRepository accountRepository;
    private final AccountHolderRepository holderRepository;

    @Override
    public Account createCheckingOrStudent(Long primaryOwnerId, Long secondaryOwnerId, Money initialBalance, String secretKey) {
        AccountHolder primary = getHolder(primaryOwnerId);
        AccountHolder secondary = secondaryOwnerId != null ? getHolder(secondaryOwnerId) : null;

        int age = Period.between(primary.getDateOfBirth(), LocalDate.now()).getYears();

        if (age < 24) {
            var acc = new StudentChecking(initialBalance, primary, secretKey);
            if (secondary != null) acc.setSecondaryOwner(secondary);
            return accountRepository.save(acc);
        } else {
            var acc = new Checking(initialBalance, primary, secretKey);
            if (secondary != null) acc.setSecondaryOwner(secondary);
            return accountRepository.save(acc);
        }
    }

    @Override
    public Savings createSavings(Long primaryOwnerId,
                                 Long secondaryOwnerId,
                                 Money initialBalance,
                                 String secretKey,
                                 Money minimumBalance,
                                 BigDecimal interestRate) {
        AccountHolder primary = getHolder(primaryOwnerId);
        AccountHolder secondary = secondaryOwnerId != null ? getHolder(secondaryOwnerId) : null;

        var min = (minimumBalance != null) ? minimumBalance : Money.of(1000);
        var rate = (interestRate != null) ? interestRate : new BigDecimal("0.0025");

        var acc = new Savings(initialBalance, primary, secondary, secretKey, min, rate);
        return accountRepository.save(acc);
    }

    public CreditCard createCreditCard(Long primaryOwnerId, Long secondaryOwnerId, Money initialBalance,
                                       Money creditLimit, BigDecimal interestRate) {
        AccountHolder primary = getHolder(primaryOwnerId);
        AccountHolder secondary = secondaryOwnerId != null ? getHolder(secondaryOwnerId) : null;

        var limit = (creditLimit != null) ? creditLimit : Money.of(100);
        var rate = (interestRate != null) ? interestRate : new BigDecimal("0.200");

        var acc = new CreditCard(initialBalance, primary, secondary, limit, rate);
        return accountRepository.save(acc);
    }

    private AccountHolder getHolder(Long id) {
        return holderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AccountHolder not found: " + id));
    }
}
