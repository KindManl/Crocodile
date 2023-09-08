package ru.nsu.fit.crocodile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.fit.crocodile.model.StatisticsData;

import java.util.Optional;

public interface StatisticsRepository extends JpaRepository<StatisticsData, Long> {
    Optional<StatisticsData> findByUser_Id(Long id);

}
