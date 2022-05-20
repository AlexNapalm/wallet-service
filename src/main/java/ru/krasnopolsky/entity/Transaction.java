package ru.krasnopolsky.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {

    private long id;
    private String playerUsername;
    private BigDecimal balanceChange;
    private LocalDateTime timestamp;
    private long balanceVersion;
    private BigDecimal balance;

    public long getId() {
        return id;
    }

    public Transaction setId(long id) {
        this.id = id;
        return this;
    }

    public String getPlayerUsername() {
        return playerUsername;
    }

    public Transaction setPlayerUsername(String playerUsername) {
        this.playerUsername = playerUsername;
        return this;
    }

    public BigDecimal getBalanceChange() {
        return balanceChange;
    }

    public Transaction setBalanceChange(BigDecimal balanceChange) {
        this.balanceChange = balanceChange;
        return this;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Transaction setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public long getBalanceVersion() {
        return balanceVersion;
    }

    public Transaction setBalanceVersion(long balanceVersion) {
        this.balanceVersion = balanceVersion;
        return this;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Transaction setBalance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", username='" + playerUsername + '\'' +
                ", balanceChange=" + balanceChange +
                ", timestamp=" + timestamp +
                ", balanceVersion=" + balanceVersion +
                ", balance=" + balance +
                '}';
    }
}
