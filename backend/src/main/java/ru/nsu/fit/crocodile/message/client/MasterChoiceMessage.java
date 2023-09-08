package ru.nsu.fit.crocodile.message.client;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MasterChoiceMessage {
    @NotNull
    private String choice;
}
