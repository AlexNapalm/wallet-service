package ru.krasnopolsky.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Player {

    @Id
    private String username;

    private long balanceVersion;

    private BigDecimal balance;

    public Player() {
        this.balanceVersion = 1L;
    }

    public String getUsername() {
        return username;
    }

    public Player setUsername(String username) {
        this.username = username;
        return this;
    }

    public long getBalanceVersion() {
        return balanceVersion;
    }

    public Player setBalanceVersion(long balanceVersion) {
        this.balanceVersion = balanceVersion;
        return this;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Player setBalance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }
}