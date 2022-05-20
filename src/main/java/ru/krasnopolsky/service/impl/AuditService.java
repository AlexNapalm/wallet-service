package ru.krasnopolsky.service.impl;

import ru.krasnopolsky.entity.Player;
import ru.krasnopolsky.entity.Transaction;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class AuditService {

    private final Logger logger = LogManager.getLogger(getClass());

    public void logTransactionIn(Transaction transaction, Player player) {
        logger.info(
                MessageFormat.format(
                        "IN by player {0}: transactionId={1}, balanceChange={2}",
                        player.getUsername(),
                        String.valueOf(transaction.getId()),
                        String.valueOf(transaction.getBalanceChange())
                )
        );
    }

    public void logTransactionOut(Transaction transaction, Player player) {
        logger.info(
                MessageFormat.format(
                        "OUT by player {0}: transactionId={1}, balanceVersion={2}, balanceChange={3}, resultingBalance={4}",
                        player.getUsername(),
                        String.valueOf(transaction.getId()),
                        String.valueOf(transaction.getBalanceVersion()),
                        String.valueOf(transaction.getBalanceChange()),
                        String.valueOf(player.getBalance())
                )
        );
    }
}
