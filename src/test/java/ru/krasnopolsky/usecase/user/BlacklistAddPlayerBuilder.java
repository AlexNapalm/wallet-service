package ru.krasnopolsky.usecase.user;

import ru.krasnopolsky.entity.Blacklist;
import ru.krasnopolsky.entity.Player;
import ru.krasnopolsky.repository.BlacklistRepository;

public class BlacklistAddPlayerBuilder {

    private final BlacklistRepository blacklistRepository;
    private final Player player;

    public BlacklistAddPlayerBuilder(BlacklistRepository blacklistRepository, Player player) {
        this.blacklistRepository = blacklistRepository;
        this.player = player;
    }

    public void build() {
        blacklistRepository.save(new Blacklist().setUsername(player.getUsername()));
    }
}
