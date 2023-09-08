package ru.nsu.fit.crocodile.message.server;

import lombok.Data;

@Data
public class RoomCreatedMessage {
    private final Long roomId;
}
