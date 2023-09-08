package ru.nsu.fit.crocodile.message.server;

import lombok.Getter;
import ru.nsu.fit.crocodile.message.client.DrawMessage;
import ru.nsu.fit.crocodile.model.Point;

@Getter
public class ImageUpdateMessage extends ServerMessage {
    Point point;
    Point prevPoint;
    String color;
    Integer width;
    private long timestamp;

    public ImageUpdateMessage(DrawMessage message) {
        this();
        point = message.getPoint();
        prevPoint = message.getPrevPoint();
        color = message.getColor();
        width = message.getWidth();
        timestamp = System.nanoTime();
    }

    public ImageUpdateMessage() {
        super(ServerMessageType.IMAGE_UPDATE);
    }
}
