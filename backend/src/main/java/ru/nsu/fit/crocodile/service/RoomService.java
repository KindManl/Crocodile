package ru.nsu.fit.crocodile.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;
import ru.nsu.fit.crocodile.exception.NoSuchElementException;
import ru.nsu.fit.crocodile.exception.PlayerAlreadyInTheRoomException;
import ru.nsu.fit.crocodile.message.client.*;
import ru.nsu.fit.crocodile.message.server.*;
import ru.nsu.fit.crocodile.model.*;

import java.security.Principal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
@Slf4j
public class RoomService {

    private final ImageWaiting waiting;

    private final MessageSender sender;

    private final ChatService chatService;

    private final AnswerGeneratorService answerGeneratorService;

    private final LobbyService lobbyService;

    private final UserDataService userDataService;

    private final StatisticsService statisticsService;

    public void joinGame(String roomId, SimpMessageHeaderAccessor headerAccessor) {
        Room room = lobbyService.getRoom(roomId);
        GreetingMessage greetingMessage;
        Player player = null;
        UserData userData;

        try {
            userData = userDataService.getUserByEmail(headerAccessor.getUser().getName());
        } catch (NoSuchElementException e) {
            greetingMessage = new GreetingMessage(-1, "Ошибка авторизации", null, null, null, 0, 0, 0L);
            sender.sendToUser(greetingMessage, headerAccessor.getUser().getName());
            return;
        }


        if (room == null) {
            greetingMessage = new GreetingMessage(-1, "Комната с таким ID не найдена", null, null, null, 0, 0, 0L);
        } else {
            if(!room.getPlayersToJoin().contains(userData.getEmail())){
                greetingMessage = new GreetingMessage(-1, "Игрок с таким именем не посылал запрос на вход", null, null, null, 0, 0, 0L);
            }
            else {
                //room.getPlayersToJoin().remove(userData.getEmail());
                try {
                    player = room.getPlayers().get(userData.getId());
                    if(player == null)
                        player = addPlayer(room, userData.getId(), userData.getUsername(), headerAccessor.getUser());
                    headerAccessor.getSessionAttributes().put("roomId", roomId);
                    headerAccessor.getSessionAttributes().put("userId", userData.getId());
                    long roundLength = room.getRoundLength();
                    long timeLeft = roundLength - (System.currentTimeMillis() - room.getStartTime());
                    greetingMessage = new GreetingMessage(0, "", new ArrayList<>(room.getPlayers().values()), room.getChat(), room.getMasterId(), roundLength, timeLeft, player.getId());
                } catch (PlayerAlreadyInTheRoomException e) {
                    greetingMessage = new GreetingMessage(-1, "Игрок с таким именем уже находится в этой комнате", null, null, null, 0, 0, 0L);
                }
            }
        }
        log.warn(greetingMessage.toString());
        sender.sendToUser(greetingMessage, headerAccessor.getUser().getName());
        if(room != null && room.getMaster() == player){
            startRound(room, room.getMasterId());
        }
        if (greetingMessage.getCode() == 0) {
            sender.sendToRoom(new NewPlayerMessage(userData.getId()), roomId);
            Player master = room.getPlayers().get(room.getMasterId());
            if (master != null) {
                askForImage(player, room.getPlayers().get(room.getMasterId()));
            }
        }
    }

    private Player addPlayer(Room room, Long id, String username, Principal principal){
        Player player = new Player(id, username, principal);
        var players = room.getPlayers();
        if (players.size() == 0)
            room.setMasterId(id);
        if (players.putIfAbsent(id, player) != null) {
            throw new PlayerAlreadyInTheRoomException();
        } else {
            return player;
        }
    }

    public void addPoint(Room room, Long id){
        room.getPlayers().get(id).getScore().addAndGet(1);
    }

    private void askForImage(Player newPlayer, Player master) {
        sender.sendToUser(new PlayerImageRequest(), master.getPrincipal().getName());
        waiting.addWaiter(master, (image -> sendImage(newPlayer, image)));
    }

    private void sendImage(Player newPlayer, Image image) {
        sender.sendToUser(image, newPlayer.getPrincipal().getName());
    }

    public void draw(DrawMessage message, SimpMessageHeaderAccessor headerAccessor) {
        Principal user = headerAccessor.getUser();
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");
        if (roomId == null) {
            sender.sendToUser(new ErrorMessage("Игрок не находится в комнате"), user.getName());
            return;
        }
        Room room = lobbyService.getRoom(roomId);
        if (room == null) {
            sender.sendToUser(new ErrorMessage("Игрок находится в несуществующей комнате"), user.getName());
            return;
        }
        if (!user.getName().equals(room.getMaster().getPrincipal().getName())) {
            sender.sendToUser(new ErrorMessage("Рисовать может только ведущий"), user.getName());
            return;
        }
        sender.sendToRoom(new ImageUpdateMessage(message), roomId);
    }

    public void sendChatMessage(ClientChatMessage message, SimpMessageHeaderAccessor headerAccessor) {
        Principal user = headerAccessor.getUser();
        Long userId = (Long) headerAccessor.getSessionAttributes().get("userId");
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");
        if (roomId == null || userId == null) {
            sender.sendToUser(new ErrorMessage("Игрок не находится в комнате"), user.getName());
            return;
        }
        Room room = lobbyService.getRoom(roomId);
        if (room == null) {
            sender.sendToUser(new ErrorMessage("Игрок находится в несуществующей комнате"), user.getName());
            return;
        }
        if (!user.getName().equals(room.getPlayers().get(userId).getPrincipal().getName())) {
            sender.sendToUser(new ErrorMessage("Попытка отправки сообщения от лица другого игрока"), user.getName());
            return;
        }

        ChatMessage chatMessage;

        try {
            var userData = userDataService.getUserByEmail(headerAccessor.getUser().getName());
            chatMessage = chatService.addMessage(room.getChat(),message.getMessage(), userId, userData.getUsername());
        } catch (NoSuchElementException e) {
            chatMessage = chatService.addMessage(room.getChat(),message.getMessage(), userId, user.getName());
        }

        if (checkAnswer(room, message.getMessage().toLowerCase()) && room.getPaused().compareAndSet(false, true)) {
            statisticsService.addWonGameAsGuessing(userId);

            statisticsService.addWonGameAsMaster(room.getMasterId());

            chatMessage.setReaction(ChatMessage.Reaction.RIGHT);
            addPoint(room, userId);
            sender.sendToRoom(chatMessage, roomId);
            room.setMasterId(userId);
            startRound(room, userId);
            return;
        }
        sender.sendToRoom(chatMessage, roomId);
    }

    private boolean checkAnswer(Room room, String answer){
        return room.getAnswer().equals(answer);
    }

    public void startRound(Room room, Long masterId) {
        sender.sendToRoom(new NewMasterMessage(masterId), room.getId());
        Set<String> answerSet = answerGeneratorService.generate();
        room.setAnswerSet(answerSet);
        sender.sendToUser(new AnswersChoice(answerSet), room.getMaster().getPrincipal().getName());
        room.getPlayers().values().stream()
                .filter(player -> !player.getId().equals(room.getMasterId()))
                .forEach(player -> statisticsService.addGameAsGuessing(player.getId()));

        statisticsService.addGameAsMaster(room.getMasterId());
    }

    private Long generateMaster(Room room) {
        Random random = new Random();
        List<Long> keys = new ArrayList<>(room.getPlayers().keySet());
        Long newMaster = keys.get(random.nextInt(keys.size()));
        room.setMasterId(newMaster);
        return newMaster;
    }

    public void react(ClientReactionMessage message, SimpMessageHeaderAccessor headerAccessor) {
        Principal user = headerAccessor.getUser();
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");
        if (roomId == null) {
            sender.sendToUser(new ErrorMessage("Игрок не находится в комнате"), user.getName());
            return;
        }
        Room room = lobbyService.getRoom(roomId);
        if (room == null) {
            sender.sendToUser(new ErrorMessage("Игрок находится в несуществующей комнате"), user.getName());
            return;
        }
        if (!user.getName().equals(room.getMaster().getPrincipal().getName())) {
            sender.sendToUser(new ErrorMessage("Попытка отправки реакции от лица другого игрока"), user.getName());
            return;
        }
        chatService.react(room.getChat(), message.getReaction(), message.getMessageId());
        sender.sendToRoom(new ServerReactionMessage(message), roomId);
    }

    public void choose(MasterChoiceMessage message, SimpMessageHeaderAccessor headerAccessor) {
        Principal user = headerAccessor.getUser();
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");
        if (roomId == null) {
            sender.sendToUser(new ErrorMessage("Игрок не находится в комнате"), user.getName());
            return;
        }
        Room room = lobbyService.getRoom(roomId);
        if (room == null) {
            sender.sendToUser(new ErrorMessage("Игрок находится в несуществующей комнате"), user.getName());
            return;
        }
        if (!user.getName().equals(room.getMaster().getPrincipal().getName())) {
            sender.sendToUser(new ErrorMessage("Попытка выбора слова от лица ведущего"), user.getName());
            return;
        }
        if(room.getAnswerSet().contains(message.getChoice())){
            room.start(message.getChoice());
            sender.sendToRoom(new RoundStartMessage(), roomId);
        }
    }


}
