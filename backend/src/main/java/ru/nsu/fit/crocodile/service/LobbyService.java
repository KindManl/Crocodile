package ru.nsu.fit.crocodile.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.nsu.fit.crocodile.message.client.JoinRoomMessage;
import ru.nsu.fit.crocodile.message.server.RoomConnectionStatus;
import ru.nsu.fit.crocodile.message.server.LobbyRoom;
import ru.nsu.fit.crocodile.message.server.LobbyRoomsMessage;
import ru.nsu.fit.crocodile.message.server.TimeLeftMessage;
import ru.nsu.fit.crocodile.model.Room;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LobbyService {

    MessageSender messageSender;

    private final ConcurrentHashMap<String, Room> rooms = new ConcurrentHashMap<>();

    public Room getRoom(String id){
        return rooms.get(id);
    }

    public String createRoom(ru.nsu.fit.crocodile.message.Client.CreateRoomMessage message){
        Room newRoom = new Room(message.getName(), message.getPassword(), message.getHidden(), message.getCapacity());;
        rooms.put(newRoom.getId(), newRoom);
        return newRoom.getId();
    }

    public RoomConnectionStatus joinRoom(JoinRoomMessage message, String userId){
        Room room = rooms.get(message.getId());
        if(!roomHasPlace(room)) {
            return new RoomConnectionStatus(null, false, "Room is full.");
        }

        if(room.getPassword() == null || room.getPassword().equals(message.getPassword())) {
            room.getPlayersToJoin().add(userId);
            return new RoomConnectionStatus(room.getId(), true, "");
        }

        else {
            return new RoomConnectionStatus(null, false, "Wrong password.");
        }
    }

    public RoomConnectionStatus joinToRandomRoom(String userId){
        Room room = findFreeRoom();
        if(room == null){
            return new RoomConnectionStatus(null, false, "No free rooms.");
        }
        room.getPlayersToJoin().add(userId);
        return new RoomConnectionStatus(room.getId(), true, "");
    }

    private boolean roomHasPlace(Room room){
        return room.getCapacity() - room.getPlayers().size() > 0;
    }

    private Room findFreeRoom(){
        return rooms.values().stream()
                .filter(room -> {
                    return !room.isHidden() && room.getPassword() == null && roomHasPlace(room);
                })
                .findAny().
                orElse(null);
    }

    public LobbyRoomsMessage getRooms(){
        var roomsList = rooms.values();
        List<LobbyRoom> rooms = roomsList.stream()
                .filter(room -> !room.isHidden())
                .map(room -> new LobbyRoom(
                        room.getId(),
                        room.getName(),
                        room.getCapacity(),
                        room.getPlayers().size(),
                        room.getPassword() != null
                )).collect(Collectors.toList());
        return new LobbyRoomsMessage(rooms);
    }

    public void clear(){
        rooms.clear();
    }
}
