package com.agorohov.restwallet.service;

import com.agorohov.restwallet.model.Wallet;
import com.agorohov.restwallet.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
public class WalletService {
    private final WalletRepository repository;

    public WalletService(WalletRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Wallet deposit(UUID id, double amount) {
        Wallet wallet = repository.findById(id).orElseThrow(() -> new WalletNotFoundException("Кошелька с таким id не существует"));
        wallet.setBalance(wallet.getBalance().add(BigDecimal.valueOf(amount)));

        return repository.save(wallet);
    }

    @Transactional
    public Wallet withdraw(UUID id, double amount) {
        return null;
    }

    public Optional<Wallet> getWallet(UUID walletId) {
        return null;
    }
}