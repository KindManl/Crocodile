package ru.nsu.fit.crocodile.message.Client;

import lombok.Data;

@Data
public class CreateRoomMessage {
    private final String name;
    private final String password;
    private final Boolean hidden;
    private final Integer capacity;
}
