package com.agorohov.restwallet.controller;

import com.agorohov.restwallet.dto.WalletOperationRequest;
import com.agorohov.restwallet.model.Wallet;
import com.agorohov.restwallet.service.WalletService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("api/v1")
public class WalletController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final WalletService service;
    public WalletController(WalletService service) {
        this.service = service;
    }
    @PostMapping("/wallet")
    public ResponseEntity<Wallet> doOperation(@Valid @RequestBody WalletOperationRequest operationRequest) {
        Wallet result = null;
        UUID walletId = operationRequest.getWalletId();
        BigDecimal amount = BigDecimal.valueOf(operationRequest.getAmount());
        switch (operationRequest.getOperationType()) {
            case DEPOSIT -> result = service.deposit(walletId, amount);
            case WITHDRAW -> result = service.withdraw(walletId, amount);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/wallets/{wallet_id}")
    public ResponseEntity<Wallet> findWallet(@PathVariable(value = "wallet_id") UUID walletId) {
        return new ResponseEntity<>(service.findById(walletId), HttpStatus.OK);
    }
}
