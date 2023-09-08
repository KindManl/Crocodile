package ru.nsu.fit.crocodile.message.server;

import lombok.Data;

import java.util.List;

@Data
public class LobbyRoomsMessage {
    private final List<LobbyRoom> rooms;
}
