package ru.krasnopolsky.service;

import ru.krasnopolsky.entity.Player;

public interface PlayerService {

    Player getOrCreate(String username);

    void updateAll();

    void blacklist(String username);

    void unblacklist(String username);

    void cleanup();

}
