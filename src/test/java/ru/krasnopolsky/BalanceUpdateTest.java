package ru.krasnopolsky;

import ru.krasnopolsky.web.dto.response.BalanceChangeResult;
import ru.krasnopolsky.exception.BusinessException;
import ru.krasnopolsky.entity.Player;
import ru.krasnopolsky.util.ExpectedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;

public class BalanceUpdateTest extends BaseTest {

    @Value("${application.config.max-balance-increase}")
    private long maxBalanceIncrease;

    @Test
    public void updateBalanceTest() {
        final Player player = createPlayer().setUsername("e.clapton").build();
        Assertions.assertEquals(BigDecimal.ZERO, player.getBalance());

        final long transactionId = 69420;
        final BalanceChangeResult result = updateBalance(player).withTransactionId(transactionId).updateBy(2000).build();

        Assertions.assertEquals(BigDecimal.valueOf(2000), result.getBalanceChange());
        Assertions.assertEquals(BigDecimal.valueOf(2000), result.getBalance());
        Assertions.assertEquals(transactionId, result.getTransactionId());
        Assertions.assertEquals(2L, result.getBalanceVersion());
    }

    @Test
    public void balanceChange_exceededLimit_exceptionThrown() {
        final Player player = createPlayer().build();

        ExpectedException.expect(
                () -> updateBalance(player).updateBy(maxBalanceIncrease + 1).build(),
                BusinessException.class,
                "error.balance-change-exceeds-max-limit"
        );
    }

    @Test
    public void balanceChange_resultsNegativeAmount_exceptionThrown() {
        final Player player = createPlayer().build();

        ExpectedException.expect(
                () -> updateBalance(player).updateBy(-2000).build(),
                BusinessException.class,
                "error.balance-less-than-zero"
        );
    }

    @Test
    public void duplicatedTransaction_returnPreviousResult() {
        final Player player = createPlayer().build();
        final long transactionId = 42069;
        updateBalance(player).withTransactionId(transactionId).updateBy(69).build();

        final BalanceChangeResult secondUpdate = updateBalance(player).withTransactionId(transactionId).updateBy(420).build();

        Assertions.assertEquals(BigDecimal.valueOf(69), secondUpdate.getBalanceChange());
        Assertions.assertEquals(BigDecimal.valueOf(69), secondUpdate.getBalance());
        Assertions.assertEquals(transactionId, secondUpdate.getTransactionId());
        Assertions.assertEquals(2L, secondUpdate.getBalanceVersion());
    }

    @Test
    public void updateBalance_correctSumCalculation() {
        final Player player = createPlayer().build();
        updateBalance(player).updateBy(5).build();
        updateBalance(player).updateBy(5).build();

        final BalanceChangeResult result = updateBalance(player).updateBy(5).build();

        Assertions.assertEquals(BigDecimal.valueOf(5), result.getBalanceChange());
        Assertions.assertEquals(BigDecimal.valueOf(15), result.getBalance());
    }

    @Test
    public void checkForOnly1000LatestTransactions() {
        final Player player = createPlayer().build();
        for (int i = 0; i < 1500; i++) {
            updateBalance(player).withTransactionId(i).updateBy(1).build();
        }

        final long transactionId = 404;
        final BalanceChangeResult result = updateBalance(player).withTransactionId(transactionId).updateBy(100).build();

        Assertions.assertEquals(BigDecimal.valueOf(100), result.getBalanceChange());
        Assertions.assertEquals(BigDecimal.valueOf(1600), result.getBalance());
        Assertions.assertEquals(transactionId, result.getTransactionId());
        Assertions.assertEquals(1502L, result.getBalanceVersion());
    }

    @Test
    public void playerInBlackList_updateBalance_exceptionThrown() {
        final Player player = createPlayer().build();
        addToBlackList(player).build();

        ExpectedException.expect(
                () -> updateBalance(player).updateBy(404).build(),
                BusinessException.class,
                "error.player-blacklisted"
        );
    }
}
