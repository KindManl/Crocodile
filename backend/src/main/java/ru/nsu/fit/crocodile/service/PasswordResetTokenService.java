package ru.nsu.fit.crocodile.service;

import org.springframework.stereotype.Service;
import ru.nsu.fit.crocodile.model.token.PasswordResetToken;
import ru.nsu.fit.crocodile.repository.PasswordResetTokenRepository;

@Service
public class PasswordResetTokenService extends TokenService<PasswordResetToken> {
    public PasswordResetTokenService(PasswordResetTokenRepository tokenRepository) {
        super(tokenRepository);
    }
}
