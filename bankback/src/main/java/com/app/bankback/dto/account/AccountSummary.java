package com.app.bankback.dto.account;


import com.app.bankback.enums.AccountStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountSummary {
    private Long id;
    private String type;
    private AccountStatus status;
    private Long primaryOwnerId;
    private String primaryOwnerName;
    private boolean hasSecondaryOwner;
    private LocalDate creationDate;
}
