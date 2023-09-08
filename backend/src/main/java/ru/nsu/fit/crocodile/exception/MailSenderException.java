package ru.nsu.fit.crocodile.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MailSenderException extends Exception{
    public MailSenderException(String message) {
        super(message);
    }

    public MailSenderException() {
        super("Error while sending an email");
    }
}
