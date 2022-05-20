package ru.krasnopolsky.web.dto.resource;

import ru.krasnopolsky.entity.Player;

public class PlayerResourceBuilder {

    public static PlayerResource create(Player player) {
        return new PlayerResource()
                .setUsername(player.getUsername())
                .setBalance(player.getBalance())
                .setBalanceVersion(player.getBalanceVersion());
    }
}
