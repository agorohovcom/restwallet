package com.agorohov.restwallet.controller;

import com.agorohov.restwallet.dto.WalletOperationRequest;
import com.agorohov.restwallet.model.Wallet;
import com.agorohov.restwallet.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1")
public class WalletController {

    private final WalletService service;

    public WalletController(WalletService service) {
        this.service = service;
    }

    @PostMapping("/wallet")
    public ResponseEntity<Wallet> doOperation(@RequestBody WalletOperationRequest operationRequest) {
        Wallet result = null;
        switch (operationRequest.getOperationType()) {
            case DEPOSIT -> result = service.deposit(operationRequest.getWalletId(), operationRequest.getAmount());
            case WITHDRAW -> result = service.withdraw(operationRequest.getWalletId(), operationRequest.getAmount());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/wallets/{wallet_id}")
    public ResponseEntity<Wallet> getWallet(@PathVariable(value = "wallet_id") UUID walletId) {
        return ResponseEntity.of(service.getWallet(walletId));
    }
}
