package ru.krasnopolsky.entity;

import ru.krasnopolsky.exception.BusinessException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.math.BigDecimal;

public class Wallet {

    private final Logger logger = LogManager.getLogger(getClass());
    private BigDecimal balance;
    private long version;

    public Wallet(Player player) {
        this.balance = player.getBalance();
        this.version = player.getBalanceVersion();
    }

    public synchronized void apply(Transaction transaction) {
        if (resultingBalanceNegative(transaction.getBalanceChange())) {
            logger.error(BusinessException.ErrorCode.BALANCE_LESS_THAN_ZERO.formatErrorCode() + " for transaction: " + transaction.toString());
            throw new BusinessException(BusinessException.ErrorCode.BALANCE_LESS_THAN_ZERO);
        }
        this.balance = balance.add(transaction.getBalanceChange());
        this.version++;
        transaction.setBalanceVersion(this.version);
        logger.info(transaction.toString());
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public long getVersion() {
        return version;
    }

    private boolean resultingBalanceNegative(BigDecimal balanceChange) {
        return this.balance.add(balanceChange).signum() == -1;
    }
}
