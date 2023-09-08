package ru.nsu.fit.crocodile.message.server;

import lombok.Data;

@Data
public class LobbyRoom {
    private final String id;
    private final String name;
    private final Integer capacity;
    private final Integer playersAmount;
    private final Boolean requiresPassword;
}
