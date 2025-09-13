package com.app.bankback.controller;


import com.app.bankback.dto.thirdparty.ThirdPartyMovementRequest;
import com.app.bankback.model.value.Money;
import com.app.bankback.service.interfaces.ThirdPartyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/third-party")
@RequiredArgsConstructor
public class ThirdPartyController {

    private static final String HEADER = "X-THIRD-PARTY-KEY";

    private final ThirdPartyService thirdPartyService;

    @PostMapping("/deposit")
    public ResponseEntity<Void> deposit(@RequestHeader(HEADER) String hashedKey,
                                         @RequestBody ThirdPartyMovementRequest request) {
        thirdPartyService.thirdPartyDeposit(
                hashedKey,
                request.getAccountId(),
                request.getAccountSecretKey(),
                Money.of(request.getAmount().doubleValue())
        );
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Void> withdraw(@RequestHeader(HEADER) String hashedKey,
                                         @RequestBody ThirdPartyMovementRequest request) {
        thirdPartyService.thirdPartyWithdraw(
                hashedKey,
                request.getAccountId(),
                request.getAccountSecretKey(),
                Money.of(request.getAmount().doubleValue())
        );
        return ResponseEntity.noContent().build();
    }
}
