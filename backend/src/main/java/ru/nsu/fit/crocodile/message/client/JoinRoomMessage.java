package ru.nsu.fit.crocodile.message.client;

import lombok.Data;

@Data
public class JoinRoomMessage {
    private final String id;
    private final String password;
}
