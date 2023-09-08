package ru.nsu.fit.crocodile.message.client;

import lombok.Data;

@Data
public class JoinMessage {
    private final Long id;
    private final String username;
}
