package ru.nsu.fit.crocodile.message.server;

import lombok.Data;

import java.util.Date;

@Data
public class StatisticsInfo {
    private final Date registrationDate;
    private final Integer friendAmount;
    private final Integer masterWins;
    private final Integer guesserWins;
    private final Float masterWinsRate;
    private final Float guesserWinsRate;
    private final Float totalWinsRate;
    private final Float rolesRate;
}
