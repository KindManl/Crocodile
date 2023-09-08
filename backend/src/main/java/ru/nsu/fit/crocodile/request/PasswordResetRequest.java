package ru.nsu.fit.crocodile.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetRequest {
    private String newPassword;

    private String newPasswordConfirm;

    private  String token;
}
