package ru.krasnopolsky.web.controller;

import ru.krasnopolsky.web.dto.request.PlayerBlacklistRequest;
import ru.krasnopolsky.web.dto.request.PlayerGetOrCreateRequest;
import ru.krasnopolsky.web.dto.request.PlayerUnblacklistRequest;
import ru.krasnopolsky.web.dto.resource.PlayerResource;
import ru.krasnopolsky.web.dto.resource.PlayerResourceBuilder;
import ru.krasnopolsky.entity.Player;
import ru.krasnopolsky.service.PlayerService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping(value = "/player/get-or-create")
    public PlayerResource getOrCreate(@Valid @RequestBody PlayerGetOrCreateRequest request) {
        final Player player = playerService.getOrCreate(request.getUsername());
        return PlayerResourceBuilder.create(player);
    }

    @PutMapping(value = "/player/blacklist")
    public void blacklist(@Valid @RequestBody PlayerBlacklistRequest request) {
        playerService.blacklist(request.getUsername());
    }

    @DeleteMapping(value = "/player/unblacklist")
    public void blacklist(@Valid @RequestBody PlayerUnblacklistRequest request) {
        playerService.unblacklist(request.getUsername());
    }
}
