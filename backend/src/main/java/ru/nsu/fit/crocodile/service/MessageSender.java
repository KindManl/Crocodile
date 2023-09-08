package ru.nsu.fit.crocodile.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.nsu.fit.crocodile.message.server.ServerMessage;

@Service
public class MessageSender {

    @Autowired
    private SimpMessagingTemplate template;

    public void sendToRoom(ServerMessage message, String roomId){
        template.convertAndSend("/topic/session/" + roomId, message);
    }

    public void sendToUser(ServerMessage message, String name){
        template.convertAndSendToUser(name, "/queue/session", message);
    }

}
