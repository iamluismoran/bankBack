package com.app.bankback.controller;

import com.app.bankback.dto.account.TransferRequest;
import com.app.bankback.model.value.Money;
import com.app.bankback.service.interfaces.AccountOperationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountsController {

    private final AccountOperationsService ops;

    /** GET balance aplicando intereses si corresponde */
    @GetMapping("/{id}/balance")
    public ResponseEntity<Map<String, Object>> getBalance(@PathVariable("id") Long accountId) {
        var balance = ops.getBalance(accountId);
        return ResponseEntity.ok(Map.of(
                "accountId", accountId,
                "balance", balance.getAmount()
        ));
    }

    /** POST transferencia entre cuentas (valida ownership, fondos, penalty…) */
    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@RequestBody TransferRequest request) {
        ops.transfer(
                request.getFromAccountId(),
                request.getToAccountId(),
                Money.of(request.getAmount()),
                request.getRequestOwnerId()
        );
        return ResponseEntity.noContent().build();
    }
}
