package de.snake.server.controller.api;

import de.snake.server.service.HistoryService;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;

@RestController
@RequestMapping("/history")
public class HistoryController {

    //todo History Features

    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping
    public ResultSet getHistory(@RequestParam String username) {
        ResultSet result = HistoryService.getPlayedGames(username);
        return result;
    }
}
