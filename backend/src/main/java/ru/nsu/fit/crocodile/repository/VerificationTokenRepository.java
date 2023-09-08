package ru.nsu.fit.crocodile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.fit.crocodile.model.UserData;
import ru.nsu.fit.crocodile.model.token.VerificationToken;

public interface VerificationTokenRepository
        extends TokenRepository<VerificationToken> {

}
