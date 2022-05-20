package ru.krasnopolsky.service.impl;

import ru.krasnopolsky.entity.Blacklist;
import ru.krasnopolsky.entity.Player;
import ru.krasnopolsky.repository.BlacklistRepository;
import ru.krasnopolsky.repository.PlayerRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.krasnopolsky.service.PlayerService;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PlayerServiceImpl implements PlayerService {

    private static final Logger LOG = LogManager.getLogger(PlayerServiceImpl.class);

    private final PlayerRepository playerRepository;
    private final BlacklistRepository blacklistRepository;
    private final Map<String, Player> storage = new ConcurrentHashMap<>();

    public PlayerServiceImpl(PlayerRepository playerRepository, BlacklistRepository blacklistRepository) {
        this.playerRepository = playerRepository;
        this.blacklistRepository = blacklistRepository;
    }

    @Override
    public synchronized Player getOrCreate(String username) {
        Player player = storage.get(username);
        if (player == null) {
            player = playerRepository.findByUsername(username)
                    .orElseGet(
                            () -> playerRepository.save(
                                    new Player()
                                            .setUsername(username)
                                            .setBalance(BigDecimal.ZERO)
                                            .setBalanceVersion(1L)
                            )
                    );
            storage.put(player.getUsername(), player);
        }
        return player;
    }

    @Override
    public void updateAll() {
        final Collection<Player> players = storage.values();
        playerRepository.saveAll(players);
        LOG.info(MessageFormat.format("Data flushed to db for a total of {0} players", players.size()));
    }

    @Override
    public void blacklist(String username) {
        final Player player = getOrCreate(username);
        blacklistRepository.save(new Blacklist().setUsername(player.getUsername()));
    }

    @Override
    public void unblacklist(String username) {
        final Player player = getOrCreate(username);
        blacklistRepository.deleteById(player.getUsername());
    }

    public void cleanup() {
        storage.clear();
    }
}