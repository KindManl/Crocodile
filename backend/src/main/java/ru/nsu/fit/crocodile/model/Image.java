package ru.nsu.fit.crocodile.model;

import lombok.*;
import ru.nsu.fit.crocodile.message.server.ServerMessage;
import ru.nsu.fit.crocodile.message.server.ServerMessageType;

@Getter
@Setter
public class Image extends ServerMessage {
    String imageData;

    public Image(String imageData) {
        this();
        this.imageData = imageData;
    }


    public Image() {
        super(ServerMessageType.IMAGE);
    }
}
