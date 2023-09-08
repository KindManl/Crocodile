package ru.nsu.fit.crocodile.message.client;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.nsu.fit.crocodile.model.Point;

import javax.validation.constraints.NotNull;

@Data
public class DrawMessage {
    final Point point;
    final Point prevPoint;
    final String color;
    final Integer width;

}
