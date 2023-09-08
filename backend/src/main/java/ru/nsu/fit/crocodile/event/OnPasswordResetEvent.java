package ru.nsu.fit.crocodile.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import ru.nsu.fit.crocodile.model.UserData;

@Getter
public class OnPasswordResetEvent extends ApplicationEvent {
    private final UserData user;

    public OnPasswordResetEvent(
            UserData user) {
        super(user);

        this.user = user;
    }
}
