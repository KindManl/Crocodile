package ru.nsu.fit.crocodile.model.token;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.nsu.fit.crocodile.model.UserData;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@DiscriminatorValue("password_reset")
@NoArgsConstructor
public class PasswordResetToken extends AbstractToken {
    public PasswordResetToken(String token, UserData user) {
        super(token, user);
    }
}
