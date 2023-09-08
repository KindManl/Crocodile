package ru.nsu.fit.crocodile.message.server;

import lombok.Getter;

@Getter
public class NewPlayerMessage extends ServerMessage{
    private Long playerId;

    public NewPlayerMessage(Long playerId) {
        this();
        this.playerId = playerId;
    }

    public NewPlayerMessage(){
        super(ServerMessageType.NEW_PLAYER);
    }

}
