package com.app.bankback.controller;

import com.app.bankback.dto.account.AccountDetail;
import com.app.bankback.dto.account.AccountSummary;
import com.app.bankback.dto.account.PageResponse;
import com.app.bankback.service.interfaces.AccountQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173") // para el front con Vite
public class AccountsQueryController {

    private final AccountQueryService queries;

    /** Listado con búsqueda y paginación (page base 1) */
    @GetMapping
    public ResponseEntity<PageResponse<AccountSummary>> list(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        if (page < 1) page = 1;
        if (pageSize < 1) pageSize = 10;
        return ResponseEntity.ok(queries.findAccounts(search, page, pageSize));
    }

    /** Detalle de una cuenta (sin balance “en vivo”) */
    @GetMapping("/{id}")
    public ResponseEntity<AccountDetail> detail(@PathVariable Long id) {
        return ResponseEntity.ok(queries.findAccountById(id));
    }
}
