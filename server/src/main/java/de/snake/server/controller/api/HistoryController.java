package de.snake.server.controller.api;

import de.snake.server.domain.entity.GameHistory;
import de.snake.server.service.HistoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/history")
public class HistoryController {

    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping
    public List<GameHistory> getHistory(@RequestParam String username) {
        return historyService.getPlayedGames(username);
    }
}
