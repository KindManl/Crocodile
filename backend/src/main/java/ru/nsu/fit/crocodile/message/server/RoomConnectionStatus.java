package ru.nsu.fit.crocodile.message.server;

import lombok.Data;

@Data
public class RoomConnectionStatus {
    private final String roomId;
    private final Boolean allowed;
    private final String message;
}
