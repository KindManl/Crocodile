package ru.nsu.fit.crocodile.message.server;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeLeftMessage extends ServerMessage{

    private Long time;

    public TimeLeftMessage(Long time){
        super(ServerMessageType.TIME_LEFT);
        this.time = time;
    }

}
