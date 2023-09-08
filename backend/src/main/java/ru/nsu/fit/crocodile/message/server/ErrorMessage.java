package ru.nsu.fit.crocodile.message.server;

import lombok.Getter;

@Getter
public class ErrorMessage extends ServerMessage {
    private String error;

    public ErrorMessage(String error){
        this();
        this.error = error;
    }

    public ErrorMessage(){
        super(ServerMessageType.ERROR);
    }
}
