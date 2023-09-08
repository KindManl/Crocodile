package ru.nsu.fit.crocodile.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.security.Principal;

@Getter
@AllArgsConstructor
public class StompPrincipal implements Principal {
    String name;
}
