package ru.nsu.fit.crocodile.message.server;

import lombok.Getter;

@Getter
public class PlayerImageRequest extends ServerMessage{
    public PlayerImageRequest() {
        super(ServerMessageType.IMAGE_REQUEST);
    }
}
