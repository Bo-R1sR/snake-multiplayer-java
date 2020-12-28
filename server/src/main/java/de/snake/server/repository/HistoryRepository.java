package de.snake.server.repository;

import de.snake.server.domain.entity.GameHistory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<GameHistory, Long> {
    List<GameHistory> findAllByUsername1(String username, Sort createdAt);
    List<GameHistory> findAllByUsername2(String username);

    List<GameHistory> findAllByUsername1OrUsername2(String username1, String username2, Sort createdAt);
}
