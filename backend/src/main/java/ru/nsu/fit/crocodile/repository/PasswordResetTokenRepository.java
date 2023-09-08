package ru.nsu.fit.crocodile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.fit.crocodile.model.token.PasswordResetToken;
import ru.nsu.fit.crocodile.model.UserData;

public interface PasswordResetTokenRepository
        extends TokenRepository<PasswordResetToken> {

}
