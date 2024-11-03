package com.agorohov.restwallet.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Objects;
import java.util.UUID;

public class WalletOperationRequest {
    @NotNull(message = "Id кошелька не может быть пустым")
    private UUID walletId;
    @NotNull(message = "Тип операции не может быть пустым")
    private OperationType operationType;
    @NotNull(message = "Количество средств для операции не может быть пустым")
    @Positive(message = "Количество средств для операции должно быть больше 0")
    private Double amount;

    public WalletOperationRequest() {
    }

    public WalletOperationRequest(UUID id, OperationType operationType, Double amount) {
        this.walletId = id;
        this.operationType = operationType;
        this.amount = amount;
    }

    public UUID getWalletId() {
        return walletId;
    }

    public void setWalletId(UUID walletId) {
        this.walletId = walletId;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WalletOperationRequest that = (WalletOperationRequest) o;
        return Objects.equals(walletId, that.walletId) && operationType == that.operationType && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(walletId, operationType, amount);
    }

    @Override
    public String toString() {
        return "WalletOperationRequest{" +
                "id=" + walletId +
                ", operationType=" + operationType +
                ", amount=" + amount +
                '}';
    }
}
