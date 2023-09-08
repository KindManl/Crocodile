package ru.nsu.fit.crocodile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import ru.nsu.fit.crocodile.model.UserData;
import ru.nsu.fit.crocodile.model.token.AbstractToken;

@NoRepositoryBean
public interface TokenRepository<T extends AbstractToken>
        extends JpaRepository<T, Long> {

    T findByToken(String token);

    T findByUser(UserData user);

    void deleteByToken(String token);

}
