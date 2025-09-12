package com.app.bankback.controller;

import com.app.bankback.dto.account.CreateCheckingRequest;
import com.app.bankback.dto.account.CreateCreditCardRequest;
import com.app.bankback.dto.account.CreateSavingsRequest;
import com.app.bankback.model.account.Account;
import com.app.bankback.model.account.CreditCard;
import com.app.bankback.model.account.Savings;
import com.app.bankback.model.value.Money;
import com.app.bankback.service.interfaces.AccountFactoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/accounts")
@RequiredArgsConstructor
public class AdminAccountsController {

    private final AccountFactoryService factory;

    @PostMapping("/checking-or-student")
    public ResponseEntity<Account> createCheckingOrStudent(@RequestBody CreateCheckingRequest request) {
        Money initial = Money.of(request.getInitialAmount().doubleValue());
        Account created = factory.createCheckingOrStudent(
                request.getPrimaryOwnerId(),
                request.getSecondaryOwnerId(),
                initial,
                request.getSecretKey()
        );
        return ResponseEntity.ok(created);
    }

    @PostMapping("/savings")
    public ResponseEntity<Savings> createSavings(@RequestBody CreateSavingsRequest request) {
        Money initial = Money.of(request.getInitialAmount().doubleValue());
        // Si vienen null el service aplica defaults/validaciones
        var minimum = (request.getMinimumBalance() != null) ? Money.of(request.getMinimumBalance().doubleValue()) : null;
        var rate = request.getInterestRate();

        Savings created = factory.createSavings(
                request.getPrimaryOwnerId(),
                request.getSecondaryOwnerId(),
                initial,
                request.getSecretKey(),
                minimum,
                rate
        );
        return ResponseEntity.ok(created);
    }

    @PostMapping("/credit-card")
    public ResponseEntity<CreditCard> createCreditCard(@RequestBody CreateCreditCardRequest request) {
        Money initial = Money.of(request.getInitialAmount().doubleValue());
        var limit = (request.getCreditLimit() != null) ? Money.of(request.getCreditLimit().doubleValue()) : null;
        var rate = request.getInterestRate();

        CreditCard created = factory.createCreditCard(
                request.getPrimaryOwnerId(),
                request.getSecondaryOwnerId(),
                initial,
                limit,
                rate
        );
        return ResponseEntity.ok(created);
    }
}
