package com.agorohov.restwallet.controller;

import com.agorohov.restwallet.dto.OperationType;
import com.agorohov.restwallet.dto.WalletOperationRequest;
import com.agorohov.restwallet.model.Wallet;
import com.agorohov.restwallet.service.WalletService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = WalletController.class)
public class WalletControllerWebMvcValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletService walletService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testDoOperationValidRequestTest() throws Exception {
        UUID walletId = UUID.randomUUID();
        WalletOperationRequest request = new WalletOperationRequest(walletId, OperationType.DEPOSIT, BigDecimal.TEN);

        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(BigDecimal.TEN);

        when(walletService.deposit(any(UUID.class), any(BigDecimal.class))).thenReturn(wallet);

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(walletId.toString()))
                .andExpect(jsonPath("$.balance").value(wallet.getBalance().toString()));
    }

    @Test
    public void testDoOperationInvalidRequestNullIdTest() throws Exception {
        WalletOperationRequest request = new WalletOperationRequest(null, OperationType.WITHDRAW, BigDecimal.TEN);

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDoOperationInvalidRequestNullOperationTypeTest() throws Exception {
        WalletOperationRequest request = new WalletOperationRequest(UUID.randomUUID(), null, BigDecimal.TEN);

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDoOperationInvalidRequestNullAmountTest() throws Exception {
        WalletOperationRequest request = new WalletOperationRequest(UUID.randomUUID(), OperationType.WITHDRAW, null);

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDoOperationInvalidRequestNegativeAmountTest() throws Exception {
        WalletOperationRequest request = new WalletOperationRequest(UUID.randomUUID(), OperationType.WITHDRAW, BigDecimal.valueOf(-5));

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}