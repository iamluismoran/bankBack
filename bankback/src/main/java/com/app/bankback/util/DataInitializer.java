package com.app.bankback.util;

import com.app.bankback.enums.AccountStatus;
import com.app.bankback.model.account.Checking;
import com.app.bankback.model.account.CreditCard;
import com.app.bankback.model.account.Savings;
import com.app.bankback.model.account.StudentChecking;
import com.app.bankback.model.user.AccountHolder;
import com.app.bankback.model.user.ThirdParty;
import com.app.bankback.model.value.Address;
import com.app.bankback.model.value.Money;
import com.app.bankback.repository.account.CheckingRepository;
import com.app.bankback.repository.account.CreditCardRepository;
import com.app.bankback.repository.account.SavingsRepository;
import com.app.bankback.repository.account.StudentCheckingRepository;
import com.app.bankback.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CheckingRepository checkingRepository;
    private final SavingsRepository savingsRepository;
    private final CreditCardRepository creditCardRepository;
    private final StudentCheckingRepository studentCheckingRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            System.out.println(">>> Seed skipped: data already present");
            return;
        }

        // ====== USERS ======
        AccountHolder felipe = new AccountHolder();
        felipe.setName("Felipe Boyle");
        felipe.setDateOfBirth(LocalDate.of(1995, 7, 12));
        Address a1 = new Address();
        a1.setCountry("US");
        a1.setStreet("123 Main St");
        a1.setCity("Springfield");
        a1.setPostalCode("01020");
        felipe.setPrimaryAddress(a1);
        felipe = userRepository.save(felipe);

        AccountHolder sofia = new AccountHolder();
        sofia.setName("Sofia Kramer");
        sofia.setDateOfBirth(LocalDate.of(1992, 3, 5));
        Address a2 = new Address();
        a2.setCountry("US");
        a2.setStreet("55 Maple Ave");
        a2.setCity("Springfield");
        a2.setPostalCode("01021");
        sofia.setPrimaryAddress(a2);
        sofia = userRepository.save(sofia);

        AccountHolder eve = new AccountHolder();
        eve.setName("Eve Linney");
        eve.setDateOfBirth(LocalDate.now().minusYears(18));
        Address a3 = new Address();
        a3.setCountry("US");
        a3.setStreet("789 Pine Rd");
        a3.setCity("Springfield");
        a3.setPostalCode("12345");
        eve.setPrimaryAddress(a3);
        eve = userRepository.save(eve);

        ThirdParty tp = new ThirdParty();
        tp.setName("Third Party API");
        tp.setHashedKey("TP-AB12CD34E");
        tp = userRepository.save(tp);

        System.out.println("++++++++ Users inserted: Felipe, Sofia, Eve, Third Party");

        // ====== ACCOUNTS ======

        // CHECKING
        Checking chk = new Checking();
        chk.setPrimaryOwner(felipe);
        chk.setSecondaryOwner(felipe); // requerido por NOT NULL
        chk.setBalance(Money.of(1200)); // <-- sin BigDecimal
        chk.setStatus(AccountStatus.ACTIVE);
        chk.setSecretKey("FEL-CHK-001");
        checkingRepository.save(chk);

        // SAVINGS
        Savings sav = new Savings();
        sav.setPrimaryOwner(sofia);
        sav.setSecondaryOwner(sofia); // requerido por NOT NULL
        sav.setBalance(Money.of(5000));
        sav.setStatus(AccountStatus.ACTIVE);
        sav.setSecretKey("SOF-SAV-001");
        sav.setMinimumBalance(Money.of(1000));
        sav.setInterestRate(new BigDecimal("0.0025"));
        sav.setLastInterestDate(LocalDate.now());
        savingsRepository.save(sav);

        // CREDIT CARD
        CreditCard cc = new CreditCard();
        cc.setPrimaryOwner(felipe);
        cc.setSecondaryOwner(felipe); // requerido por NOT NULL
        cc.setBalance(Money.of(200));
        cc.setStatus(AccountStatus.ACTIVE);
        cc.setCreditLimit(Money.of(1500));
        cc.setInterestRate(new BigDecimal("0.200"));
        cc.setLastInterestDate(LocalDate.now());
        creditCardRepository.save(cc);

        // STUDENT CHECKING
        StudentChecking stu = new StudentChecking();
        stu.setPrimaryOwner(eve);
        stu.setSecondaryOwner(eve); // requerido por NOT NULL
        stu.setBalance(Money.of(300));
        stu.setStatus(AccountStatus.ACTIVE);
        stu.setSecretKey("EVE-STU-001");
        studentCheckingRepository.save(stu);

        System.out.println("++++++++ Accounts inserted: Checking, Savings, Credit Card, StudentChecking");
        System.out.println(">>> Seed OK");
    }
}
