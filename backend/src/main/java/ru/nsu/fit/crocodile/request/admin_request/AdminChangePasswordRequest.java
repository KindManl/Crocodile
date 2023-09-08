package ru.nsu.fit.crocodile.request.admin_request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminChangePasswordRequest {
    private String email;
    private String oldPassword;
    private String newPassword;
}
