package ru.nsu.fit.crocodile.message.client;

import lombok.Data;
import ru.nsu.fit.crocodile.model.ChatMessage;

@Data
public class ClientReactionMessage {
    private final Integer messageId;
    private final ChatMessage.Reaction reaction;
}
