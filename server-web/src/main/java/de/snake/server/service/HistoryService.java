package de.snake.server.service;

import de.snake.server.config.WebSocketEventListener;
import de.snake.server.domain.entity.GameHistory;
import de.snake.server.domain.game.Playground;
import de.snake.server.repository.HistoryRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryService {
    private final HistoryRepository historyRepository;
    private final Playground playground;
    private final WebSocketEventListener webSocketEventListener;

    public HistoryService(HistoryRepository historyRepository, Playground playground, WebSocketEventListener webSocketEventListener) {
        this.historyRepository = historyRepository;
        this.playground = playground;
        this.webSocketEventListener = webSocketEventListener;
    }

    public List<GameHistory> getPlayedGames(String username) {
        return historyRepository.findAllByUsername1OrUsername2(username, username, Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public void saveGame() {
        GameHistory gameHistory = new GameHistory();
        gameHistory.setPointsUser1(playground.getSnake1().getPoints());
        gameHistory.setPointsUser2(playground.getSnake2().getPoints());
        gameHistory.setUsername1(webSocketEventListener.getUsername1());
        gameHistory.setUsername2(webSocketEventListener.getUsername2());
        historyRepository.save(gameHistory);
    }
}
