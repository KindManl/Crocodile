package ru.nsu.fit.crocodile.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequest {
    private String email;
    private String username;
    private String password;
}
