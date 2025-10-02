package com.app.bankback.service.impl;

import com.app.bankback.dto.account.AccountDetail;
import com.app.bankback.dto.account.AccountSummary;
import com.app.bankback.dto.account.PageResponse;
import com.app.bankback.model.account.Account;
import com.app.bankback.model.account.Checking;
import com.app.bankback.model.account.CreditCard;
import com.app.bankback.model.account.Savings;
import com.app.bankback.model.account.StudentChecking;
import com.app.bankback.repository.account.AccountRepository;
import com.app.bankback.service.interfaces.AccountQueryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountQueryServiceImpl implements AccountQueryService {

    private final AccountRepository accountRepository;

    @Override
    public PageResponse<AccountSummary> findAccounts(String search, int page, int pageSize) {
        // Normaliza parámetros
        if (page < 1) page = 1;
        if (pageSize < 1) pageSize = 10;

        final String s = (search == null) ? "" : search.trim();
        final String sLower = s.toLowerCase(Locale.ROOT);
        final boolean isNumeric = !s.isBlank() && s.matches("\\d+");
        final Long idFilter = isNumeric ? Long.valueOf(s) : null;

        // Cargamos todas (volumen pequeño) y filtramos en memoria
        List<Account> all = accountRepository.findAll();

        List<Account> filtered = all.stream()
                .filter(a -> {
                    if (s.isBlank()) return true;
                    // Nombre del titular principal
                    String ownerName = a.getPrimaryOwner() != null && a.getPrimaryOwner().getName() != null
                            ? a.getPrimaryOwner().getName().toLowerCase(Locale.ROOT)
                            : "";
                    boolean matchName = ownerName.contains(sLower);
                    boolean matchId = isNumeric && a.getId().equals(idFilter);
                    return matchName || matchId;
                })
                .collect(Collectors.toList());

        // Paginación segura
        int fromIndex = Math.max(0, (page - 1) * pageSize);
        if (fromIndex >= filtered.size()) {
            return PageResponse.<AccountSummary>builder()
                    .items(List.of())
                    .total(filtered.size())
                    .page(page)
                    .pageSize(pageSize)
                    .build();
        }
        int toIndex = Math.min(filtered.size(), fromIndex + pageSize);

        List<AccountSummary> summaries = filtered.subList(fromIndex, toIndex).stream()
                .map(this::toSummary)
                .collect(Collectors.toList());

        return PageResponse.<AccountSummary>builder()
                .items(summaries)
                .total(filtered.size())
                .page(page)
                .pageSize(pageSize)
                .build();
    }

    @Override
    public AccountDetail findAccountById(Long id) {
        Account acc = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
        return toDetail(acc);
    }

    // ===================== Helpers / Mappers =====================

    private String typeOf(Account acc) {
        if (acc instanceof Checking) return "CHECKING";
        if (acc instanceof Savings) return "SAVINGS";
        if (acc instanceof CreditCard) return "CREDIT_CARD";
        if (acc instanceof StudentChecking) return "STUDENT_CHECKING";
        return "ACCOUNT";
    }

    private AccountSummary toSummary(Account acc) {
        return AccountSummary.builder()
                .id(acc.getId())
                .type(typeOf(acc))
                .status(acc.getStatus())
                .primaryOwnerId(acc.getPrimaryOwner() != null ? acc.getPrimaryOwner().getId() : null)
                .primaryOwnerName(acc.getPrimaryOwner() != null ? acc.getPrimaryOwner().getName() : null)
                .hasSecondaryOwner(acc.getSecondaryOwner() != null)
                .creationDate(acc.getCreationDate())
                .build();
    }

    private AccountDetail toDetail(Account acc) {
        AccountDetail.AccountDetailBuilder builder = AccountDetail.builder()
                .id(acc.getId())
                .type(typeOf(acc))
                .status(acc.getStatus())
                .primaryOwnerId(acc.getPrimaryOwner() != null ? acc.getPrimaryOwner().getId() : null)
                .primaryOwnerName(acc.getPrimaryOwner() != null ? acc.getPrimaryOwner().getName() : null)
                .secondaryOwnerId(acc.getSecondaryOwner() != null ? acc.getSecondaryOwner().getId() : null)
                .hasSecondaryOwner(acc.getSecondaryOwner() != null)
                .creationDate(acc.getCreationDate());

        // Tags simples para la UI
        List<String> tags = new ArrayList<>();
        tags.add(typeOf(acc));
        tags.add(acc.getStatus().name());
        builder.tags(tags);

        // Detalles por subtipo (sin exponer secretKey ni penaltyFee)
        if (acc instanceof Checking chk) {
            builder.checking(AccountDetail.CheckingInfo.builder()
                    .minimumBalance(chk.getMinimumBalance().getAmount())
                    .monthlyMaintenanceFee(chk.getMonthlyMaintenanceFee().getAmount())
                    .lastMonthlyFeeDate(chk.getLastMonthlyFeeDate())
                    .build());
        } else if (acc instanceof Savings sav) {
            builder.savings(AccountDetail.SavingsInfo.builder()
                    .minimumBalance(sav.getMinimumBalance().getAmount())
                    .interestRate(sav.getInterestRate())
                    .lastInterestDate(sav.getLastInterestDate())
                    .build());
        } else if (acc instanceof CreditCard cc) {
            builder.creditCard(AccountDetail.CreditCardInfo.builder()
                    .creditLimit(cc.getCreditLimit().getAmount())
                    .interestRate(cc.getInterestRate())
                    .lastInterestDate(cc.getLastInterestDate())
                    .build());
        } else if (acc instanceof StudentChecking) {
            builder.studentChecking(new AccountDetail.StudentCheckingInfo());
        }

        return builder.build();
    }
}