package ru.nsu.fit.crocodile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.crocodile.UserDataUtils;
import ru.nsu.fit.crocodile.dto.UserDto;
import ru.nsu.fit.crocodile.exception.ChangePasswordException;
import ru.nsu.fit.crocodile.exception.ElementAlreadyExistException;
import ru.nsu.fit.crocodile.exception.NoSuchElementException;
import ru.nsu.fit.crocodile.model.Status;
import ru.nsu.fit.crocodile.model.UserData;
import ru.nsu.fit.crocodile.request.admin_request.*;
import ru.nsu.fit.crocodile.service.UserDataService;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserDataService userDataService;

    @GetMapping("/get-by-email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) throws NoSuchElementException {
        return new ResponseEntity<>(UserDataUtils.userdataToDto(userDataService.getUserByEmail(email)), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Long> register(@RequestBody AdminRegistrationRequest request) throws ElementAlreadyExistException {
        UserData user = userDataService.createUser(request.getEmail(), request.getUsername(),
                request.getPassword(), Status.ACTIVE, request.getRoles());
        return new ResponseEntity<>(user.getId(), HttpStatus.OK);
    }

    @PostMapping("/change-name")
    public ResponseEntity<HttpStatus> changeName(@RequestBody AdminChangeNameRequest request) throws NoSuchElementException {
        userDataService.changeName(request.getEmail(), request.getNewName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/change-password")
    public ResponseEntity<HttpStatus> changePassword(@RequestBody AdminChangePasswordRequest request) throws NoSuchElementException, ChangePasswordException {
        userDataService.changePassword(request.getEmail(), request.getOldPassword(), request.getNewPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/send-friend-request")
    public ResponseEntity<HttpStatus> sendFriendRequest(@RequestBody AdminFriendRequest request) throws ElementAlreadyExistException, NoSuchElementException {
        userDataService.sendFriendRequest(request.getFromEmail(), request.getToEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/accept-friend-request")
    public ResponseEntity<HttpStatus> acceptFriendRequest(@RequestBody AdminAcceptFriendRequest request) throws NoSuchElementException {
        userDataService.acceptFriendRequest(request.getWhoReceivedRequestEmail(), request.getWhoSentRequestEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/get-friends/{email}")
    public ResponseEntity<List<UserDto>> getFriends(@PathVariable String email) throws NoSuchElementException {
        return new ResponseEntity<>(userDataService.getFriends(email), HttpStatus.OK);
    }

    @GetMapping("/get-incoming-requests/{email}")
    public ResponseEntity<List<UserDto>> getIncomingFriendRequests(@PathVariable String email) throws NoSuchElementException {
        return new ResponseEntity<>(userDataService.getIncomingFriendRequests(email), HttpStatus.OK);
    }

    @GetMapping("/get-outcoming-requests/{email}")
    public ResponseEntity<List<UserDto>> getOutcomingFriendRequests(@PathVariable String email) throws NoSuchElementException {
        List<UserDto> list = userDataService.getOutcomingFriendRequests(email);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/delete-friend")
    public ResponseEntity<HttpStatus> deleteFriend(@RequestBody AdminDeleteFriendRequest request) throws NoSuchElementException {
        userDataService.deleteFriend(request.getUserEmail(), request.getFriendEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete-user-by-id")
    public ResponseEntity<HttpStatus> deleteUserById(@RequestParam Long id) {
        userDataService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete-user-by-email")
    public ResponseEntity<HttpStatus> deleteUserByEmail(@RequestParam String email) {
        userDataService.deleteUser(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
