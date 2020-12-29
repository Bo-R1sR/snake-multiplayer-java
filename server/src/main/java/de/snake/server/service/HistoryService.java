package de.snake.server.service;

import de.snake.server.domain.entity.GameHistory;
import de.snake.server.repository.HistoryRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class HistoryService {
    private final HistoryRepository historyRepository;

    public HistoryService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public List<GameHistory> getPlayedGames(String username){
        return historyRepository.findAllByUsername1OrUsername2(username, username, Sort.by(Sort.Direction.DESC, "createdAt"));
    }
    public void save(Integer pointsUser1, Integer pointsUser2, String username1, String username2){
        GameHistory gameHistory = new GameHistory();
        gameHistory.setPointsUser1(pointsUser1);
        gameHistory.setPointsUser2(pointsUser2);
        gameHistory.setUsername1(username1);
        gameHistory.setUsername2(username2);
        historyRepository.save(gameHistory);
    }
}
