package com.app.bankback.dto.account;

import com.app.bankback.enums.AccountStatus;
import com.app.bankback.model.account.Checking;
import com.app.bankback.model.account.CreditCard;
import com.app.bankback.model.account.Savings;
import com.app.bankback.model.account.StudentChecking;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDetail {

    private Long id;
    private String type;
    private AccountStatus status;
    private Long primaryOwnerId;
    private String primaryOwnerName;
    private Long secondaryOwnerId;
    private boolean hasSecondaryOwner;
    private LocalDate creationDate;

    // Utiles para type, status, etc..
    private List<String> tags;

    private CheckingInfo checking;
    private SavingsInfo savings;
    private CreditCardInfo creditCard;
    private StudentCheckingInfo studentChecking;

    @Data @NoArgsConstructor @AllArgsConstructor
    @Builder @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CheckingInfo {
        private BigDecimal minimumBalance;
        private BigDecimal monthlyMaintenanceFee;
        private LocalDate lastMonthlyFeeDate;
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    @Builder @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SavingsInfo {
        private BigDecimal minimumBalance;
        private BigDecimal interestRate;
        private LocalDate lastInterestDate;
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    @Builder @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CreditCardInfo {
        private BigDecimal creditLimit;
        private BigDecimal interestRate;
        private LocalDate lastInterestDate;
    }
    // Para distinguir el tipo en JSON con {}
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class StudentCheckingInfo {}
}
