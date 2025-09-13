package com.app.bankback.controller;

import com.app.bankback.dto.account.CreateCheckingRequest;
import com.app.bankback.dto.account.CreateCreditCardRequest;
import com.app.bankback.dto.account.CreateSavingsRequest;
import com.app.bankback.enums.AccountStatus;
import com.app.bankback.model.account.Account;
import com.app.bankback.model.account.CreditCard;
import com.app.bankback.model.account.Savings;
import com.app.bankback.model.value.Money;
import com.app.bankback.repository.account.AccountRepository;
import com.app.bankback.service.interfaces.AccountFactoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/admin/accounts")
@RequiredArgsConstructor
public class AdminAccountsController {

    private final AccountFactoryService factory;
    private final AccountRepository accounts;

    @PostMapping("/checking-or-student")
    public ResponseEntity<Account> createCheckingOrStudent(@RequestBody CreateCheckingRequest request) {
        Money initial = Money.of(request.getInitialAmount());
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

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestParam("status") AccountStatus status) {
        var acc = accounts.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        acc.setStatus(status);
        accounts.save(acc);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id]")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        if (!accounts.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
        accounts.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
