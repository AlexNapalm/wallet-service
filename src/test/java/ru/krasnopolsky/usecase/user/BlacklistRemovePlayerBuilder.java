package ru.krasnopolsky.usecase.user;

import ru.krasnopolsky.entity.Player;
import ru.krasnopolsky.repository.BlacklistRepository;

public class BlacklistRemovePlayerBuilder {

    private final BlacklistRepository blacklistRepository;
    private final Player player;

    public BlacklistRemovePlayerBuilder(BlacklistRepository blacklistRepository, Player player) {
        this.blacklistRepository = blacklistRepository;
        this.player = player;
    }

    public void build() {
        blacklistRepository.deleteById(player.getUsername());
    }
}
