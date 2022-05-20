package ru.krasnopolsky.web.dto.resource;

import java.math.BigDecimal;

public class PlayerResource {

    private String username;
    private long balanceVersion;
    private BigDecimal balance;

    public String getUsername() {
        return username;
    }

    public PlayerResource setUsername(String username) {
        this.username = username;
        return this;
    }

    public long getBalanceVersion() {
        return balanceVersion;
    }

    public PlayerResource setBalanceVersion(long balanceVersion) {
        this.balanceVersion = balanceVersion;
        return this;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public PlayerResource setBalance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }
}
