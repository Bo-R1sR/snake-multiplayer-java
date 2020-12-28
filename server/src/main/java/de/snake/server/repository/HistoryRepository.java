package de.snake.server.repository;

import de.snake.server.domain.entity.GameHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<GameHistory, Long> {
}
