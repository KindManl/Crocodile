package ru.nsu.fit.crocodile.request.admin_request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdminRegistrationRequest {
    private String email;
    private String username;
    private String password;
    private List<String> roles;
}
