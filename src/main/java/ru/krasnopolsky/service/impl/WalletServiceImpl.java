package ru.krasnopolsky.service.impl;

import ru.krasnopolsky.web.dto.response.BalanceChangeResult;
import ru.krasnopolsky.exception.BusinessException;
import ru.krasnopolsky.entity.Player;
import ru.krasnopolsky.entity.Transaction;
import ru.krasnopolsky.entity.Wallet;
import ru.krasnopolsky.repository.BlacklistRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.krasnopolsky.service.LockService;
import ru.krasnopolsky.service.PlayerService;
import ru.krasnopolsky.service.TransactionService;
import ru.krasnopolsky.service.WalletService;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService {

    private final PlayerService playerService;
    private final TransactionService transactionService;
    private final BlacklistRepository blacklistRepository;
    private final LockService lockService;
    private final AuditService auditService;
    private final long maxBalanceIncrease;

    public WalletServiceImpl(PlayerService playerService,
                             TransactionService transactionService,
                             BlacklistRepository blacklistRepository,
                             LockService lockService,
                             AuditService auditService,
                             @Value("${application.config.max-balance-increase}") long maxBalanceIncrease) {
        this.playerService = playerService;
        this.transactionService = transactionService;
        this.blacklistRepository = blacklistRepository;
        this.lockService = lockService;
        this.auditService = auditService;
        this.maxBalanceIncrease = maxBalanceIncrease;
    }

    @Override
    public BalanceChangeResult updateBalance(String username, long transactionId, BigDecimal balanceChange) {
        if (balanceChange.longValue() > maxBalanceIncrease) {
            throw new BusinessException(BusinessException.ErrorCode.BALANCE_CHANGE_EXCEEDS_MAX_LIMIT);
        }

        final Player player = playerService.getOrCreate(username);
        if (blacklistRepository.findById(player.getUsername()).isPresent()) {
            throw new BusinessException(BusinessException.ErrorCode.PLAYER_BLACKLISTED);
        }

        lockService.lock(player.getUsername());
        try {
            final Optional<Transaction> existingTransaction = transactionService.findById(player, transactionId);
            if (existingTransaction.isPresent()) {
                final Transaction transaction = existingTransaction.stream()
                        .filter(t -> Objects.equals(transactionId, t.getId()))
                        .findFirst()
                        .orElseThrow(EntityNotFoundException::new);

                return new BalanceChangeResult()
                        .setTransactionId(transactionId)
                        .setBalanceChange(transaction.getBalanceChange())
                        .setBalanceVersion(transaction.getBalanceVersion())
                        .setBalance(transaction.getBalance().add(transaction.getBalanceChange()));
            }

            final Wallet wallet = new Wallet(player);
            final Transaction transaction = new Transaction()
                    .setId(transactionId)
                    .setPlayerUsername(player.getUsername())
                    .setTimestamp(LocalDateTime.now())
                    .setBalanceChange(balanceChange)
                    .setBalance(wallet.getBalance());
            wallet.apply(transaction);

            auditService.logTransactionIn(transaction, player);

            transactionService.add(transaction);
            player.setBalance(wallet.getBalance());
            player.setBalanceVersion(wallet.getVersion());

            auditService.logTransactionOut(transaction, player);

            return new BalanceChangeResult()
                    .setTransactionId(transactionId)
                    .setBalanceChange(balanceChange)
                    .setBalanceVersion(wallet.getVersion())
                    .setBalance(wallet.getBalance());
        } finally {
            lockService.unlock(player.getUsername());
        }
    }

}