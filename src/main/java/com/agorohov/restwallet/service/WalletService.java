package com.agorohov.restwallet.service;

import com.agorohov.restwallet.exception.NotEnoughBalanceException;
import com.agorohov.restwallet.exception.TooLargeBalanceException;
import com.agorohov.restwallet.exception.WalletNotFoundException;
import com.agorohov.restwallet.model.Wallet;
import com.agorohov.restwallet.repository.WalletRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class WalletService {
    Logger logger = LoggerFactory.getLogger(WalletService.class);

    private final WalletRepository repository;

    public WalletService(WalletRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Wallet deposit(UUID id, BigDecimal amount) {
        logger.info("Метод deposit был вызван с параметрами {} и {}", id, amount);

        Wallet wallet = findById(id);
        BigDecimal newBalance = wallet.getBalance().add(amount);
        BigDecimal limit = new BigDecimal("9999999999999999");
        if (newBalance.compareTo(limit) > 0) {
            throw new TooLargeBalanceException("На балансе не может быть больше, чем 9999999999999999");
        }
        wallet.setBalance(newBalance);
        return repository.save(wallet);
    }

    @Transactional
    public Wallet withdraw(UUID id, BigDecimal amount) {
        logger.info("Метод withdraw был вызван с параметрами {} и {}", id, amount);

        Wallet wallet = findById(id);
        BigDecimal newBalance = wallet.getBalance().subtract(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new NotEnoughBalanceException("На балансе недостаточно средств для операции");
        }
        wallet.setBalance(newBalance);
        return repository.save(wallet);
    }

    public Wallet findById(UUID walletId) {
        logger.info("Метод findById был вызван с параметром {}", walletId);

        return repository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Кошелёк не найден"));
    }
}