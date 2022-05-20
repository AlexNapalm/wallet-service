package ru.krasnopolsky.usecase.user;

import ru.krasnopolsky.entity.Player;
import ru.krasnopolsky.service.PlayerService;
import net.bytebuddy.utility.RandomString;

public class CreatePlayerBuilder {

    private final PlayerService playerService;
    private String username = RandomString.make(8);

    public CreatePlayerBuilder(PlayerService playerService) {
        this.playerService = playerService;
    }

    public CreatePlayerBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public Player build() {
        return playerService.getOrCreate(username);
    }
}
