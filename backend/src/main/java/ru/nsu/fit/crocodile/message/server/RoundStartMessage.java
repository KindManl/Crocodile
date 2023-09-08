package ru.nsu.fit.crocodile.message.server;

import lombok.Getter;

@Getter
public class RoundStartMessage extends ServerMessage{
    public RoundStartMessage() {
        super(ServerMessageType.ROUND_START);
    }
}
