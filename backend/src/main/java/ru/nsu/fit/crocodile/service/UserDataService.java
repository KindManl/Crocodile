package ru.nsu.fit.crocodile.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.nsu.fit.crocodile.UserDataUtils;
import ru.nsu.fit.crocodile.dto.UserDto;
import ru.nsu.fit.crocodile.exception.BadTokenException;
import ru.nsu.fit.crocodile.exception.ChangePasswordException;
import ru.nsu.fit.crocodile.exception.ElementAlreadyExistException;
import ru.nsu.fit.crocodile.exception.NoSuchElementException;
import ru.nsu.fit.crocodile.model.StatisticsData;
import ru.nsu.fit.crocodile.model.Status;
import ru.nsu.fit.crocodile.model.UserData;
import ru.nsu.fit.crocodile.model.token.PasswordResetToken;
import ru.nsu.fit.crocodile.model.token.VerificationToken;
import ru.nsu.fit.crocodile.repository.UserRepository;

import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDataService {

    private final UserRepository userRepository;

    private final RoleService roleService;
    private final TokenService<VerificationToken> verificationTokenService;

    private final TokenService<PasswordResetToken> passwordResetTokenService;

    private final BCryptPasswordEncoder encoder;

    public UserData getUserByEmail(String email) throws NoSuchElementException {
        return userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    @Transactional
    public UserData createUser(String email, String name, String password, Status status, List<String> roles) throws ElementAlreadyExistException {
        UserData user = new UserData();
        user.setEmail(email);
        user.setPassword(encoder.encode(password));
        user.setUsername(name);
        user.setStatus(status);
        user.setUserRoles(roleService.mapStringToRoles(roles));
        createUser(user);
        return user;
    }

    public UserData createUser(UserData user) throws ElementAlreadyExistException {
        if (userRepository.existsByEmail(user.getEmail())) {
            log.info("User with such email already exists");
            throw new ElementAlreadyExistException("User with such email already exists");
        }
        userRepository.save(user);
        return user;
    }

    @Transactional
    public void activateUser(String token) throws BadTokenException {
        VerificationToken verificationToken = verificationTokenService.getToken(token);

        UserData user = verificationToken.getUser();
        long secLeft = ChronoUnit.SECONDS.between(LocalDateTime.now(), verificationToken.getExpiryDate());
        if (secLeft <= 0) {
            throw new BadTokenException("Token has expired");
        }

        user.setStatus(Status.ACTIVE);
        userRepository.save(user);
        verificationTokenService.deleteByToken(token);
    }

    @Transactional
    public void confirmPasswordReset(String token, String oldPassword, String newPassword) throws BadTokenException, ChangePasswordException {
        PasswordResetToken resetToken = passwordResetTokenService.getToken(token);
        UserData user = resetToken.getUser();
        long secLeft = ChronoUnit.SECONDS.between(LocalDateTime.now(), resetToken.getExpiryDate());

        if (secLeft <= 0) {
            throw new BadTokenException("Token has expired");
        }

        changePassword(user, oldPassword, newPassword);
        passwordResetTokenService.deleteByToken(token);
    }

    @Transactional
    public void changeName(String email, String newName) throws NoSuchElementException {
        UserData user = getUserByEmail(email);
        user.setUsername(newName);
        userRepository.save(user);
    }

    @Transactional
    public void changePassword(String email, String oldPassword, String newPassword) throws ChangePasswordException, NoSuchElementException {
        UserData user = getUserByEmail(email);
        changePassword(user, oldPassword, newPassword);
    }

    public void changePassword(UserData user, String oldPassword, String newPassword) throws ChangePasswordException{
        if (encoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(encoder.encode(newPassword));
        } else {
            log.info("Old password doesn't match existing");
            throw new ChangePasswordException("Old password doesn't match existing");
        }
        userRepository.save(user);
    }

    @Transactional
    public void sendFriendRequest(String srcEmail, String rcvEmail) throws ElementAlreadyExistException, NoSuchElementException {
        UserData srcUser = getUserByEmail(srcEmail);
        UserData rcvUser = getUserByEmail(rcvEmail);
        List<UserData> incReq = userRepository.findAllByOutcomingFriendRequestsContains(srcUser);
        List<UserData> outcReq = userRepository.findAllByIncomingFriendRequestsContains(srcUser);
        if (incReq.contains(rcvUser)) return;
        if (outcReq.contains(rcvUser)) {
            log.info("Already have incoming request from this user");
            throw new ElementAlreadyExistException("Already have incoming request from this user");
        }
        srcUser.getOutcomingFriendRequests().add(rcvUser);
        userRepository.save(srcUser);
    }

    @Transactional
    public void acceptFriendRequest(String whoReceivedRequestEmail, String whoSentRequestEmail) throws NoSuchElementException {
        UserData whoReceivedRequestUser = getUserByEmail(whoReceivedRequestEmail);
        UserData whoSentRequestUser = getUserByEmail(whoSentRequestEmail);
        List<UserData> incReq = userRepository.findAllByOutcomingFriendRequestsContains(whoReceivedRequestUser);
        if (!incReq.contains(whoSentRequestUser)) {
            log.info("No request from this user");
            throw new NoSuchElementException("No request from this user");
        }
        whoReceivedRequestUser.getFriends().add(whoSentRequestUser);
        whoSentRequestUser.getOutcomingFriendRequests().remove(whoReceivedRequestUser);
        whoSentRequestUser.getFriends().add(whoReceivedRequestUser);

        userRepository.save(whoReceivedRequestUser);
        userRepository.save(whoSentRequestUser);
    }

    @Transactional
    public void deleteFriend(String userEmail, String friendEmail) throws NoSuchElementException {
        UserData user = getUserByEmail(userEmail);
        UserData friend = getUserByEmail(friendEmail);
        user.getFriends().remove(friend);
        friend.getFriends().remove(user);
        friend.getOutcomingFriendRequests().add(user);

        userRepository.save(user);
        userRepository.save(friend);
    }

    public List<UserDto> getFriends(String email) throws NoSuchElementException {
        UserData user = getUserByEmail(email);
        return UserDataUtils.userdataToDto(userRepository.findAllByFriendsContains(user));
    }

    public List<UserDto> getIncomingFriendRequests(String email) throws NoSuchElementException {
        UserData user = getUserByEmail(email);
        return UserDataUtils.userdataToDto(userRepository.findAllByOutcomingFriendRequestsContains(user));
    }

    public List<UserDto> getOutcomingFriendRequests(String email) throws NoSuchElementException {
        UserData user = getUserByEmail(email);
        return UserDataUtils.userdataToDto(userRepository.findAllByIncomingFriendRequestsContains(user));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void deleteUser(String email) {
        userRepository.deleteByEmail(email);
    }
}
