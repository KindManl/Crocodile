package ru.nsu.fit.crocodile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.fit.crocodile.message.server.StatisticsInfo;
import ru.nsu.fit.crocodile.model.StatisticsData;
import ru.nsu.fit.crocodile.model.UserData;
import ru.nsu.fit.crocodile.repository.StatisticsRepository;
import ru.nsu.fit.crocodile.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class StatisticsService {

    private final UserRepository userRepository;
    private final StatisticsRepository statisticsRepository;

    @Transactional
    public void addGameAsMaster(Long id){
        StatisticsData statistics = statisticsRepository.findByUser_Id(id).orElseThrow();
        statistics.setMasterGames(statistics.getMasterGames() + 1);
        statisticsRepository.save(statistics);
    }

    @Transactional
    public void addGameAsGuessing(Long id){
        StatisticsData statistics = statisticsRepository.findByUser_Id(id).orElseThrow();
        statistics.setGuessingGames(statistics.getGuessingGames() + 1);
        statisticsRepository.save(statistics);
    }

    @Transactional
    public void addWonGameAsMaster(Long id){
        StatisticsData statistics = statisticsRepository.findByUser_Id(id).orElseThrow();
        statistics.setMasterWins(statistics.getMasterWins() + 1);
        statisticsRepository.save(statistics);
    }

    @Transactional
    public void addWonGameAsGuessing(Long id){
        StatisticsData statistics = statisticsRepository.findByUser_Id(id).orElseThrow();
        statistics.setGuessingWins(statistics.getGuessingGames() + 1);
        statisticsRepository.save(statistics);
    }

    public StatisticsInfo getStatistics(String email){
        var user = userRepository.findByEmail(email).orElseThrow();
        StatisticsData statistics = user.getStatistics();
        return new StatisticsInfo(
                user.getRegistrationDate(),
                user.getFriends().size(),
                statistics.getMasterWins(),
                statistics.getGuessingWins(),
                (float) statistics.getMasterWins() / statistics.getMasterGames(),
                (float)statistics.getGuessingWins() / statistics.getGuessingGames(),
                (float)(statistics.getMasterWins() + statistics.getGuessingWins()) / statistics.getGamesPlayed(),
                (float)statistics.getMasterGames() / statistics.getGuessingGames()
        );
    }

}
