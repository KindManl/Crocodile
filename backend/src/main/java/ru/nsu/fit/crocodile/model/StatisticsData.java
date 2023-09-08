package ru.nsu.fit.crocodile.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "statistics_data")
public class StatisticsData {
    @Id
    @GeneratedValue
    private Integer id;

    private Integer gamesPlayed = 0;

    private Integer GuessingWins = 0;

    private Integer MasterWins = 0;

    private Integer GuessingGames = 0;

    private Integer MasterGames = 0;

    @OneToOne(mappedBy = "statistics")
    private UserData user;
}
