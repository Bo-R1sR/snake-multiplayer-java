package de.snake.server.controller;

import de.snake.server.service.HistoryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/history")
public class HistoryController {

    //todo History Features

    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @PostMapping
    public String getHistory(@RequestBody String username) {
        System.out.println(username);
        return ("hello from server");
    }

    @GetMapping
    public String getHistory2(@RequestParam String username) {
        System.out.println(username);
        return "good to go";
    }
}
