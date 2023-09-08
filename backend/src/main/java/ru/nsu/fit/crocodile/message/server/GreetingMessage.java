package ru.nsu.fit.crocodile.message.server;

import lombok.Getter;
import ru.nsu.fit.crocodile.model.Chat;
import ru.nsu.fit.crocodile.model.Player;

import java.util.List;

@Getter
public class GreetingMessage extends ServerMessage{
    private int code;
    private String message;
    private List<Player> players;
    private Chat chat;
    private Long master;
    private long roundTime;
    private long timeLeft;
    private Long user;

    public GreetingMessage(int code, String message, List<Player> players, Chat chat, Long master, long roundTime, long timeLeft, Long user) {
        this();
        this.code = code;
        this.message = message;
        this.players = players;
        this.chat = chat;
        this.master = master;
        this.roundTime = roundTime;
        this.timeLeft = timeLeft;
        this.user = user;
    }

    public GreetingMessage(){
        super(ServerMessageType.GREETING);
    }

    @Override
    public String toString() {
        return "GreetingMessage{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", players=" + players +
                ", chat=" + chat +
                ", master='" + master + '\'' +
                ", roundTime=" + roundTime +
                ", timeLeft=" + timeLeft +
                '}';
    }
}
