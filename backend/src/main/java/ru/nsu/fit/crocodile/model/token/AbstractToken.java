package ru.nsu.fit.crocodile.model.token;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.nsu.fit.crocodile.model.UserData;
import ru.nsu.fit.crocodile.service.TokenService;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
@DiscriminatorColumn(name = "token_type")
public class AbstractToken {
    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    private LocalDateTime expiryDate;

    @OneToOne(targetEntity = UserData.class, fetch = FetchType.EAGER)
    @MapsId
    private UserData user;

    public AbstractToken(String token, UserData user) {
        this.token = token;
        this.user = user;
        expiryDate = TokenService.calculateExpiryDate(EXPIRATION);
    }

}
