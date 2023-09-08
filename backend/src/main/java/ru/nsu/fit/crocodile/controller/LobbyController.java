package ru.nsu.fit.crocodile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.crocodile.message.Client.CreateRoomMessage;
import ru.nsu.fit.crocodile.message.client.JoinRoomMessage;
import ru.nsu.fit.crocodile.message.server.LobbyRoomsMessage;
import ru.nsu.fit.crocodile.message.server.RoomConnectionStatus;
import ru.nsu.fit.crocodile.message.server.RoomIdMessage;
import ru.nsu.fit.crocodile.service.LobbyService;

@RestController
@RequestMapping("/lobby")
@RequiredArgsConstructor
public class LobbyController {
    private final LobbyService lobbyService;

    @PostMapping("/create")
    public ResponseEntity<RoomIdMessage> createRoom(Authentication auth, @RequestBody CreateRoomMessage message){
        String id = lobbyService.createRoom(message);
        return new ResponseEntity<>(new RoomIdMessage(id), HttpStatus.OK);
    }

    @PostMapping("/join")
    public ResponseEntity<RoomConnectionStatus> joinRoom(Authentication auth, @RequestBody JoinRoomMessage message){
        var answer = lobbyService.joinRoom(message, auth.getName());
        if(answer.getRoomId() == null){
            return new ResponseEntity<>(answer, HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(answer, HttpStatus.OK);
    }

    @PostMapping("/join-random")
    public ResponseEntity<RoomConnectionStatus> joinRandomRoom(Authentication auth){
        var answer = lobbyService.joinToRandomRoom(auth.getName());
        if(answer.getRoomId() == null){
            return new ResponseEntity<>(answer, HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(answer, HttpStatus.OK);
    }

    @GetMapping("/rooms")
    public ResponseEntity<LobbyRoomsMessage> getRooms(){
        return new ResponseEntity<>(lobbyService.getRooms(), HttpStatus.OK);
    }

    @PostMapping("/clear")
    public void clear(){
        lobbyService.clear();
    }

}
