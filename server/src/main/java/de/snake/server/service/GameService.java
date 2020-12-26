package de.snake.server.service;

import de.snake.server.controller.GameController;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private final GameController gameController;


    public GameService(GameController gameController) {
        this.gameController = gameController;
    }

    public void startCounter() throws InterruptedException {
        gameController.startCounter();
    }
}
