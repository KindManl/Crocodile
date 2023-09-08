package ru.nsu.fit.crocodile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nsu.fit.crocodile.model.Role;
import ru.nsu.fit.crocodile.model.UserData;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

    boolean existsByName(String name);

    List<Role> findAllByUsersContains(UserData user);

    List<Role> findAllByNameIn(List<String> name);
}
