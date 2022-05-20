package ru.krasnopolsky.service.impl;

import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.krasnopolsky.service.PlayerService;

@Component
@Profile("!test")
public class PeriodicScheduler {

    private final PlayerService playerService;

    public PeriodicScheduler(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Scheduled(fixedDelayString = "${application.config.memory-flush-interval}")
    public void flushDataToDb() {
        playerService.updateAll();
    }
}
