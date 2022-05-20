package ru.krasnopolsky.service;

import ru.krasnopolsky.web.dto.response.BalanceChangeResult;

import java.math.BigDecimal;

public interface WalletService {

    /**
     * @param username      username
     * @param transactionId transaction identifier
     * @param balanceChange balance change
     */
    BalanceChangeResult updateBalance(String username, long transactionId, BigDecimal balanceChange);
}
