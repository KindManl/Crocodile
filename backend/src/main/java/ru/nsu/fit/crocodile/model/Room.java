package ru.nsu.fit.crocodile.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import ru.nsu.fit.crocodile.exception.PlayerAlreadyInTheRoomException;

import java.security.Principal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
@Setter
public class Room {

    private String id;
    private String name;
    private String password;
    private ConcurrentHashMap<Long, Player> players = new ConcurrentHashMap<>();
    private Set<String> playersToJoin = new HashSet<>();
    private Long masterId = null;
    private String answer = "default";
    private Chat chat;
    private AtomicBoolean paused = new AtomicBoolean(false);
    private Set<String> answerSet;
    private final long roundLength = 180000;
    private final Timer timer = new Timer();
    private long startTime;
    private boolean hidden;
    private int capacity;

    public Room(String name, String password, boolean hidden, int capacity) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.password = password;
        this.hidden = hidden;
        this.capacity = capacity;
        this.chat = new Chat();
    }

    public void start(String answer){
        this.answer = answer;
        paused.set(false);
        startTime = System.currentTimeMillis();
    }

    public Player getMaster() {
        return players.get(masterId);
    }
}
