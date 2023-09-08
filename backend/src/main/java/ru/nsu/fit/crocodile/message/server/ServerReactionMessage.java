package ru.nsu.fit.crocodile.message.server;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.fit.crocodile.message.client.ClientReactionMessage;
import ru.nsu.fit.crocodile.model.ChatMessage;

@Getter
@Setter
public class ServerReactionMessage extends ServerMessage{
    private Integer messageId;
    private ChatMessage.Reaction reaction;

    public ServerReactionMessage(ClientReactionMessage message){
        this(message.getMessageId(), message.getReaction());
    }

    private ServerReactionMessage(Integer messageId, ChatMessage.Reaction reaction) {
        this();
        this.messageId = messageId;
        this.reaction = reaction;
    }

    public ServerReactionMessage(){
        super(ServerMessageType.REACTION);
    }
}
