package ru.nsu.fit.crocodile.message.server;

import lombok.Getter;

@Getter
public class ServerMessage {
    private ServerMessageType type;

    public ServerMessage(ServerMessageType type) {
        this.type = type;
    }
}
