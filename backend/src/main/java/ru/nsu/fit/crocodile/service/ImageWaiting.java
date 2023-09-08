package ru.nsu.fit.crocodile.service;

import org.springframework.stereotype.Component;
import ru.nsu.fit.crocodile.model.Image;
import ru.nsu.fit.crocodile.model.Player;

import java.security.Principal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ImageWaiting {
    private Map<String, ArrayList<ImageSender>> waitingUsers = new ConcurrentHashMap<>();

    public void addWaiter(Player master, ImageSender callback){
        waitingUsers.merge(master.getPrincipal().getName(), new ArrayList<>(Collections.singletonList(callback)), (old, n) -> {
            old.add(callback);
            return old;
        });
    }

    public void forwardImage(Principal master, Image image){
        ArrayList<ImageSender> waiters = waitingUsers.get(master.getName());
        if(waiters != null) {
            waiters.forEach(imageSender -> imageSender.send(image));
            waiters.clear();
        }
    }
}