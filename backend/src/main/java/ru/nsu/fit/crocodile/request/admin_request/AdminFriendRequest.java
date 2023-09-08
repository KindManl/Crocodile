package ru.nsu.fit.crocodile.request.admin_request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AdminFriendRequest {
    private String fromEmail;
    private String toEmail;
}
