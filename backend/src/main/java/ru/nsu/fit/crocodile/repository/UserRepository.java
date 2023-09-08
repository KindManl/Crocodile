package ru.nsu.fit.crocodile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.fit.crocodile.model.Role;
import ru.nsu.fit.crocodile.model.UserData;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserData, Long> {
    Optional<UserData> findByEmail(String email);

    boolean existsByEmail(String email);

    UserData findByUsername(String username);

    List<UserData> findAllByFriendsContains(UserData user);

    List<UserData> findAllByIncomingFriendRequestsContains(UserData user);

    UserData findByIncomingFriendRequests(UserData user);

    List<UserData> findAllByOutcomingFriendRequestsContains(UserData user);

    UserData findByOutcomingFriendRequests(UserData user);

    void deleteByEmail(String email);
}
