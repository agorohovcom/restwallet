package com.agorohov.restwallet.service;

import com.agorohov.restwallet.exception.NotEnoughBalanceException;
import com.agorohov.restwallet.exception.TooLargeBalanceException;
import com.agorohov.restwallet.exception.WalletNotFoundException;
import com.agorohov.restwallet.model.Wallet;
import com.agorohov.restwallet.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @InjectMocks
    private WalletService out;

    @Mock
    private WalletRepository repositoryMock;

    private UUID walletId;
    private UUID failWalletId;
    private BigDecimal balance;
    private Wallet wallet;

    @BeforeEach
    void setUp() {
        walletId = UUID.randomUUID();
        failWalletId = UUID.randomUUID();
        balance = new BigDecimal("1000");
        wallet = new Wallet(walletId, balance);
    }

    @Test
    void depositCorrectTest() {
        BigDecimal amount = new BigDecimal("350");
        Wallet excepted = new Wallet(walletId, wallet.getBalance().add(amount));

        when(repositoryMock.findById(walletId)).thenReturn(Optional.of(wallet));
        when(repositoryMock.save(any(Wallet.class))).thenReturn(excepted);

        Wallet actual = out.deposit(walletId, amount);

        assertNotNull(actual);
        assertEquals(excepted, actual);
        assertEquals(excepted.getId(), actual.getId());
        assertEquals(excepted.getBalance(), actual.getBalance());
    }

    @Test
    void depositTooLargeBalanceTest() {
        BigDecimal tooLargeAmount = new BigDecimal("99999999999999999");

        when(repositoryMock.findById(walletId)).thenReturn(Optional.of(wallet));

        assertThrows(TooLargeBalanceException.class, () -> out.deposit(walletId, tooLargeAmount));
    }

    @Test
    void depositIncorrectIdTest() {
        when(repositoryMock.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () -> out.deposit(failWalletId, any(BigDecimal.class)));
    }

    @Test
    void withdrawCorrectTest() {
        BigDecimal amount = new BigDecimal("350");
        Wallet excepted = new Wallet(walletId, wallet.getBalance().subtract(amount));

        when(repositoryMock.findById(walletId)).thenReturn(Optional.of(wallet));
        when(repositoryMock.save(any(Wallet.class))).thenReturn(excepted);

        Wallet actual = out.withdraw(walletId, amount);

        assertNotNull(actual);
        assertEquals(excepted, actual);
        assertEquals(excepted.getId(), actual.getId());
        assertEquals(excepted.getBalance(), actual.getBalance());
    }

    @Test
    void withdrawFailTest() {
        BigDecimal amount = balance.add(BigDecimal.ONE);
        System.out.println(amount);

        when(repositoryMock.findById(walletId)).thenReturn(Optional.of(wallet));

        assertThrows(NotEnoughBalanceException.class, () -> out.withdraw(walletId, amount));
    }

    @Test
    void withdrawIncorrectIdTest() {
        when(repositoryMock.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () -> out.withdraw(failWalletId, any(BigDecimal.class)));
    }

    @Test
    void findByIdExistsTest() {
        when(repositoryMock.findById(walletId)).thenReturn(Optional.of(wallet));

        Wallet actual = out.findById(walletId);

        assertNotNull(actual);
        assertEquals(wallet, actual);
        assertEquals(wallet.getId(), actual.getId());
        assertEquals(wallet.getBalance(), actual.getBalance());
    }

    @Test
    void findByIdNotExistsTest() {
        when(repositoryMock.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () -> out.findById(failWalletId));
    }
}