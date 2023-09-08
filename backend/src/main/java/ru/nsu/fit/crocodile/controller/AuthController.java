package ru.nsu.fit.crocodile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.nsu.fit.crocodile.event.OnRegistrationCompleteEvent;
import ru.nsu.fit.crocodile.exception.*;
import ru.nsu.fit.crocodile.model.RoleEnum;
import ru.nsu.fit.crocodile.model.Status;
import ru.nsu.fit.crocodile.model.UserData;
import ru.nsu.fit.crocodile.request.RegistrationRequest;
import ru.nsu.fit.crocodile.service.UserDataService;

import java.util.Collections;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserDataService userDataService;

    private final ApplicationEventPublisher eventPublisher;

    private final BCryptPasswordEncoder encoder;

    @PostMapping("/login")
    public ResponseEntity<HttpStatus> login(@RequestParam String email, @RequestParam String password) throws NoSuchElementException, UserStatusException {
        UserData user = userDataService.getUserByEmail(email);
        if (user.getStatus() != Status.ACTIVE) {
            throw new UserStatusException("User is not active");
        }
        if (encoder.matches(password, user.getPassword())) return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PostMapping("/register")
    public ResponseEntity<Long> register(@RequestBody RegistrationRequest request) throws ElementAlreadyExistException, MailSenderException {
        UserData user = userDataService.createUser(request.getEmail(), request.getUsername(),
                request.getPassword(), Status.NOT_ENABLED, Collections.singletonList(RoleEnum.USER.name()));
        try {
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user));
        } catch (MailException e) {
            throw new MailSenderException();
        }
        return new ResponseEntity<>(user.getId(), HttpStatus.OK);
    }

    @GetMapping("/registration-confirm")
    public ResponseEntity<String> confirmRegistration(@RequestParam("token") String token) throws BadTokenException {
        String content =
                "<header>"
                        + "<p>Registration succeed</p>"
                        + "</header>";
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.TEXT_HTML);
        userDataService.activateUser(token);
        return new ResponseEntity<>(content, responseHeaders, HttpStatus.OK);
    }
}
