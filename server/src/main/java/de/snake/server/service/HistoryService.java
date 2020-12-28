package de.snake.server.service;

import de.snake.server.domain.entity.GameHistory;
import de.snake.server.repository.HistoryRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryService {
    private final HistoryRepository historyRepository;

    public HistoryService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public List<GameHistory> getPlayedGames(String username){
        return historyRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }
}
