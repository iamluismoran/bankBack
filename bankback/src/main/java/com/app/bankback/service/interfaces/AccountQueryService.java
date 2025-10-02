package com.app.bankback.service.interfaces;

import com.app.bankback.dto.account.AccountDetail;
import com.app.bankback.dto.account.AccountSummary;
import com.app.bankback.dto.account.PageResponse;

public interface AccountQueryService {

    PageResponse<AccountSummary> findAccounts(String search, int page, int pageSize);

    AccountDetail findAccountById(Long id);
}
