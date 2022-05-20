package ru.krasnopolsky;

import org.springframework.dao.EmptyResultDataAccessException;
import ru.krasnopolsky.entity.Blacklist;
import ru.krasnopolsky.entity.Player;
import ru.krasnopolsky.repository.PlayerRepository;
import ru.krasnopolsky.util.ExpectedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class PlayerTest extends BaseTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    public void schedulerFlushTest() {
        final String username = "l.messi";
        final Player player = createPlayer().setUsername(username).build();
        for (int i = 0; i < 100; i++) {
            updateBalance(player).updateBy(5).build();
        }

        playerService.updateAll();

        final Optional<Player> playerFromDb = playerRepository.findByUsername(player.getUsername());
        Assertions.assertTrue(playerFromDb.isPresent());
        Assertions.assertEquals(username, playerFromDb.get().getUsername());
        Assertions.assertEquals(new BigDecimal("500.00"), playerFromDb.get().getBalance());
        Assertions.assertEquals(101L, playerFromDb.get().getBalanceVersion());
    }

    @Test
    public void addPlayerToBlacklistTest() {
        final Player player = createPlayer().build();

        addToBlackList(player).build();

        final Optional<Blacklist> blacklist = blacklistRepository.findById(player.getUsername());
        Assertions.assertEquals(player.getUsername(), blacklist.get().getUsername());
    }

    @Test
    public void removePlayerFromBlacklistTest() {
        final Player player = createPlayer().build();
        addToBlackList(player).build();

        removeFromBlacklist(player).build();

        Assertions.assertFalse(blacklistRepository.findById(player.getUsername()).isPresent());
    }

    @Test
    public void multipleAddingPlayerToBlacklist() {
        final Player player = createPlayer().build();
        addToBlackList(player).build();

        addToBlackList(player).build();

        final List<Blacklist> allBlacklistedPlayers = blacklistRepository.findAll();
        Assertions.assertEquals(1, allBlacklistedPlayers.size());
        Assertions.assertEquals(player.getUsername(), allBlacklistedPlayers.get(0).getUsername());
    }

    @Test
    public void removeFromBlackList_playerThatNotInBlacklist_exceptionThrown() {
        final Player player = createPlayer().build();

        ExpectedException.expect(
                () -> removeFromBlacklist(player).build(),
                EmptyResultDataAccessException.class
        );
    }
}
