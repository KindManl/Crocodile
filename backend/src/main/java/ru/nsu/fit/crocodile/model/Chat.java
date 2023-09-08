package ru.nsu.fit.crocodile.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Chat {
    private List<ChatMessage> messages = new ArrayList<>();
}
