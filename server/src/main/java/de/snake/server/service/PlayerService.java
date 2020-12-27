package de.snake.server.service;

import de.snake.server.controller.websocket.GameController;
import de.snake.server.domain.game.Playground;
import de.snake.server.domain.game.ScreenText;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {
    private final Playground playground;
    private final GameController gameController;
    private final ScreenText screenText;
    private final SimpMessagingTemplate template;

    public PlayerService(Playground playground, GameController gameController, ScreenText screenText, SimpMessagingTemplate template) {
        this.playground = playground;
        this.gameController = gameController;
        this.screenText = screenText;
        this.template = template;
    }

    public void startCounter() throws InterruptedException {
        if (!playground.isRunning()) {
            playground.setRunning(true);
            gameController.startCounter();
        }
    }

    public void setScreenText() {
        screenText.setPlayerText("auf anderen Spieler warten");
        // send screen text to client
        template.convertAndSend("/topic/screenText", screenText);
    }
}
