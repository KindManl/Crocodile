package ru.nsu.fit.crocodile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.fit.crocodile.exception.BadTokenException;
import ru.nsu.fit.crocodile.model.token.AbstractToken;
import ru.nsu.fit.crocodile.repository.TokenRepository;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class TokenService<T extends AbstractToken> {

    private final TokenRepository<T> tokenRepository;

    public T getToken(String token) throws BadTokenException {
        T tokenToReturn = tokenRepository.findByToken(token);
        if (tokenToReturn == null) throw new BadTokenException("No such token");
        return tokenToReturn;
    }

    @Transactional
    public void createToken(T token) {
        tokenRepository.save(token);
    }

    public void deleteByToken(String token) {
        tokenRepository.deleteByToken(token);
    }

    public static LocalDateTime calculateExpiryDate(int expiryTimeInMinutes) {
        return LocalDateTime.now().plusMinutes(expiryTimeInMinutes);
    }
}
