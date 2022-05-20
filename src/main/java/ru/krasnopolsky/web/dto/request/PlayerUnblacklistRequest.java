package ru.krasnopolsky.web.dto.request;

import javax.validation.constraints.NotNull;

public class PlayerUnblacklistRequest {

    @NotNull
    private String username;

    public String getUsername() {
        return username;
    }

    public PlayerUnblacklistRequest setUsername(String username) {
        this.username = username;
        return this;
    }
}
