package ru.nsu.fit.crocodile.message.client;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ClientChatMessage {
    @NotNull
    private String message;
}
