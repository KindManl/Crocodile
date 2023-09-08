package ru.nsu.fit.crocodile.service;

import org.springframework.stereotype.Service;
import ru.nsu.fit.crocodile.model.token.VerificationToken;
import ru.nsu.fit.crocodile.repository.VerificationTokenRepository;

@Service
public class VerificationTokenService extends TokenService<VerificationToken>{
    public VerificationTokenService(VerificationTokenRepository tokenRepository) {
        super(tokenRepository);
    }
}
