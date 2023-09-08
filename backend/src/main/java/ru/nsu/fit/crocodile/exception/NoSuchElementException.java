package ru.nsu.fit.crocodile.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class NoSuchElementException extends Exception {
    public NoSuchElementException(String message) {
        super(message);
    }
}
