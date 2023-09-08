package ru.nsu.fit.crocodile.model.token;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.nsu.fit.crocodile.model.UserData;

import javax.persistence.*;

@Getter
@Setter
@Entity
@DiscriminatorValue("verification")
@NoArgsConstructor
public class VerificationToken extends AbstractToken {
    public VerificationToken(String token, UserData user) {
        super(token, user);
    }
}
