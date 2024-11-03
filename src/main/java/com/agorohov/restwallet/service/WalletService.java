package com.agorohov.restwallet.service;

//import com.agorohov.restwallet.exception.IncorrectFundsAmountException;

import com.agorohov.restwallet.exception.NotEnoughBalanceException;
import com.agorohov.restwallet.exception.WalletNotFoundException;
import com.agorohov.restwallet.model.Wallet;
import com.agorohov.restwallet.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class WalletService {
    private final WalletRepository repository;

    public WalletService(WalletRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Wallet deposit(UUID id, BigDecimal amount) {
//        checkAmountIsNotNegative(amount);
        Wallet wallet = findById(id);
        wallet.setBalance(wallet.getBalance().add(amount));
        return repository.save(wallet);
    }

    @Transactional
    public Wallet withdraw(UUID id, BigDecimal amount) {
//        checkAmountIsNotNegative(amount);
        Wallet wallet = findById(id);
        BigDecimal newBalance = wallet.getBalance().subtract(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new NotEnoughBalanceException("На балансе недостаточно средств");
        }
        wallet.setBalance(newBalance);
        return repository.save(wallet);
    }

    public Wallet findById(UUID walletId) {
        return repository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Кошелёк не найден"));
    }

    // Для этого сделал валидацию. Удалить если пройдёт тесты
//    private void checkAmountIsNotNegative(BigDecimal amount) {
//        if(!(amount.compareTo(BigDecimal.ZERO) > 0)) {
//            throw new IncorrectFundsAmountException("Количество средств для операции должно быть больше 0");
//        }
//    }
}