package ru.krasnopolsky.web.dto.request;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class BalanceChangeRequest {

    @NotNull
    private String username;

    @NotNull
    private Long transactionId;

    @NotNull
    private BigDecimal balanceChange;

    public String getUsername() {
        return username;
    }

    public BalanceChangeRequest setUsername(String username) {
        this.username = username;
        return this;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public BalanceChangeRequest setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public BigDecimal getBalanceChange() {
        return balanceChange;
    }

    public BalanceChangeRequest setBalanceChange(BigDecimal balanceChange) {
        this.balanceChange = balanceChange;
        return this;
    }
}
