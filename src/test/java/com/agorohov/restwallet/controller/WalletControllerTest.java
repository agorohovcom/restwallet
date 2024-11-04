package com.agorohov.restwallet.controller;

import com.agorohov.restwallet.dto.OperationType;
import com.agorohov.restwallet.dto.WalletOperationRequest;
import com.agorohov.restwallet.exception.TooLargeBalanceException;
import com.agorohov.restwallet.exception.WalletNotFoundException;
import com.agorohov.restwallet.model.Wallet;
import com.agorohov.restwallet.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletControllerTest {

    @InjectMocks
    private WalletController out;

    @Mock
    private WalletService serviceMock;

    private Wallet wallet;

    @BeforeEach
    void setUp() {
        wallet = new Wallet();
        wallet.setId(UUID.randomUUID());
        wallet.setBalance(BigDecimal.valueOf(1000));
    }

    @Test
    void doOperationCorrectDepositTest() {
        WalletOperationRequest request = new WalletOperationRequest();
        request.setWalletId(wallet.getId());
        request.setAmount(BigDecimal.valueOf(50));
        request.setOperationType(OperationType.DEPOSIT);

        when(serviceMock.deposit(any(UUID.class), any(BigDecimal.class))).thenReturn(wallet);

        ResponseEntity<Wallet> actual = out.doOperation(request);

        assertNotNull(actual);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(wallet, actual.getBody());
    }

    @Test
    void doOperationTooLargeDepositTest() {
        WalletOperationRequest request = new WalletOperationRequest();
        request.setWalletId(wallet.getId());
        request.setAmount(new BigDecimal("9999999999999999"));
        request.setOperationType(OperationType.DEPOSIT);

        when(serviceMock.deposit(any(UUID.class), any(BigDecimal.class))).thenThrow(TooLargeBalanceException.class);

        assertThrows(TooLargeBalanceException.class, () -> out.doOperation(request));
    }

    @Test
    void doOperationCorrectWithdrawTest() {
        WalletOperationRequest request = new WalletOperationRequest();
        request.setWalletId(wallet.getId());
        request.setAmount(BigDecimal.valueOf(50));
        request.setOperationType(OperationType.WITHDRAW);

        when(serviceMock.withdraw(any(UUID.class), any(BigDecimal.class))).thenReturn(wallet);

        ResponseEntity<Wallet> actual = out.doOperation(request);

        assertNotNull(actual);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(wallet, actual.getBody());
    }

    @Test
    void findWalletExistsTest() {
        when(serviceMock.findById(wallet.getId())).thenReturn(wallet);

        ResponseEntity<Wallet> actual = out.findWallet(wallet.getId());

        assertNotNull(actual);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(wallet, actual.getBody());
    }

    @Test
    void findWalletNotExistsTest() {
        when(serviceMock.findById(any(UUID.class))).thenThrow(WalletNotFoundException.class);

        assertThrows(WalletNotFoundException.class, () -> out.findWallet(wallet.getId()));
    }
}