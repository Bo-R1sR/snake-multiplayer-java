package de.snake.server.controller.websocket;

import de.snake.server.domain.game.Playground;
import de.snake.server.service.PlayerService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class PlayerController {
    private final PlayerService playerService;
    private final Playground playground;

    public PlayerController(PlayerService playerService, Playground playground) {
        this.playerService = playerService;
        this.playground = playground;
    }


    // if clients request start of game
    // method has to be called by both players
    @MessageMapping("/playerActive/{id}")
    public void waitForAllPlayers(@DestinationVariable int id) throws InterruptedException {

        if (id == 1) playground.setPlayer1active(true);
        if (id == 2) playground.setPlayer2active(true);
        if (playground.getPlayer1active() && playground.getPlayer2active()) {
            playerService.startCounter();
        } else {
            playerService.setScreenText();

        }
    }
}
