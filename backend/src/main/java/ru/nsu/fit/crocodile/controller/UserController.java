package ru.nsu.fit.crocodile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.crocodile.UserDataUtils;
import ru.nsu.fit.crocodile.dto.UserDto;
import ru.nsu.fit.crocodile.exception.ChangePasswordException;
import ru.nsu.fit.crocodile.exception.ElementAlreadyExistException;
import ru.nsu.fit.crocodile.exception.NoSuchElementException;
import ru.nsu.fit.crocodile.message.server.StatisticsInfo;
import ru.nsu.fit.crocodile.event.OnPasswordResetEvent;
import ru.nsu.fit.crocodile.event.OnRegistrationCompleteEvent;
import ru.nsu.fit.crocodile.exception.*;
import ru.nsu.fit.crocodile.model.UserData;
import ru.nsu.fit.crocodile.request.*;
import ru.nsu.fit.crocodile.service.StatisticsService;
import ru.nsu.fit.crocodile.service.UserDataService;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserDataService userDataService;
    private final StatisticsService statisticsService;

    private final ApplicationEventPublisher eventPublisher;

    @GetMapping("/get-info")
    public ResponseEntity<UserDto> getInfo(Authentication auth) throws NoSuchElementException {
        return new ResponseEntity<>(UserDataUtils.userdataToDto(userDataService.getUserByEmail(auth.getName())), HttpStatus.OK);
    }

    @PostMapping("/password-reset")
    public ResponseEntity<HttpStatus> resetPassword(@RequestParam("email") String userEmail) throws NoSuchElementException, MailSenderException {
        UserData user = userDataService.getUserByEmail(userEmail);
        try {
            eventPublisher.publishEvent(new OnPasswordResetEvent(user));
        } catch (MailException e) {
            throw new MailSenderException();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/password-reset-confirm")
    public ResponseEntity<HttpStatus> confirmPasswordReset(@RequestBody PasswordResetRequest request) throws BadTokenException, ChangePasswordException {
        userDataService.confirmPasswordReset(request.getToken(), request.getNewPassword(), request.getNewPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/change-name")
    public ResponseEntity<HttpStatus> changeName(Authentication auth,
                                                 @RequestBody ChangeNameRequest request) throws NoSuchElementException {
        userDataService.changeName(auth.getName(), request.getNewName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/change-password")
    public ResponseEntity<HttpStatus> changePassword(Authentication auth,
                                                     @RequestBody ChangePasswordRequest request) throws NoSuchElementException, ChangePasswordException {
        userDataService.changePassword(auth.getName(), request.getOldPassword(), request.getNewPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/send-friend-request")
    public ResponseEntity<HttpStatus> sendFriendRequest(Authentication auth,
                                                        @RequestBody FriendRequest request) throws ElementAlreadyExistException, NoSuchElementException {
        userDataService.sendFriendRequest(auth.getName(), request.getToEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/accept-friend-request")
    public ResponseEntity<HttpStatus> acceptFriendRequest(Authentication auth,
                                                          @RequestBody AcceptFriendRequest request) throws NoSuchElementException {
        userDataService.acceptFriendRequest(auth.getName(), request.getWhoSentRequestEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/get-friends")
    public ResponseEntity<List<UserDto>> getFriends(Authentication auth) throws NoSuchElementException {
        return new ResponseEntity<>(userDataService.getFriends(auth.getName()), HttpStatus.OK);
    }

    @GetMapping("/get-incoming-requests")
    public ResponseEntity<List<UserDto>> getIncomingFriendRequests(Authentication auth) throws NoSuchElementException {
        return new ResponseEntity<>(userDataService.getIncomingFriendRequests(auth.getName()), HttpStatus.OK);
    }

    @GetMapping("/get-outcoming-requests")
    public ResponseEntity<List<UserDto>> getOutcomingFriendRequests(Authentication auth) throws NoSuchElementException {
        return new ResponseEntity<>(userDataService.getOutcomingFriendRequests(auth.getName()), HttpStatus.OK);
    }

    @PostMapping("/delete-friend")
    public ResponseEntity<HttpStatus> deleteFriend(Authentication auth, @RequestBody DeleteFriendRequest request) throws NoSuchElementException {
        userDataService.deleteFriend(auth.getName(), request.getFriendEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/get-stats")
    public ResponseEntity<StatisticsInfo> getStatistics(Authentication auth){
        return new ResponseEntity<>(statisticsService.getStatistics(auth.getName()), HttpStatus.OK);
    }
}
