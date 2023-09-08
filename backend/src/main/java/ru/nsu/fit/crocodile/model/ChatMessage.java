package ru.nsu.fit.crocodile.model;

import lombok.*;
import ru.nsu.fit.crocodile.message.server.ServerMessage;
import ru.nsu.fit.crocodile.message.server.ServerMessageType;

@Getter
@Setter
public class ChatMessage extends ServerMessage {

    @ToString
    public enum Reaction{
        NO_REACTION, LIKE, DISLIKE, RIGHT
    }

    private String message;
    private Long userId;
    private Integer MessageId;
    private Reaction reaction;
    private String username;

    public ChatMessage(String message, Long userId, Integer messageId, Reaction reaction, String username) {
        this();
        this.message = message;
        this.userId = userId;
        MessageId = messageId;
        this.reaction = reaction;
        this.username = username;
    }

    public ChatMessage() {
        super(ServerMessageType.CHAT_MESSAGE);
    }
}
