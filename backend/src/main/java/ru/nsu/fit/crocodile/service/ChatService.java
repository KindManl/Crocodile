package ru.nsu.fit.crocodile.service;

import org.springframework.stereotype.Service;
import ru.nsu.fit.crocodile.exception.MessageNotFoundException;
import ru.nsu.fit.crocodile.model.Chat;
import ru.nsu.fit.crocodile.model.ChatMessage;

@Service
public class ChatService {

    public ChatMessage addMessage(Chat chat, String message, Long userId, String name) {
        ChatMessage newMessage = new ChatMessage(message, userId, chat.getMessages().size(), ChatMessage.Reaction.NO_REACTION, name);
        chat.getMessages().add(newMessage);
        return newMessage;
    }

    public void react(Chat chat, ChatMessage.Reaction reaction, Integer messageId) {
        ChatMessage message = chat.getMessages().get(messageId);
        if (message == null) {
            throw new MessageNotFoundException();
        }
        message.setReaction(reaction);
    }

}
