package de.snake.server.controller.websocket;

import de.snake.server.domain.game.Playground;
import de.snake.server.domain.game.SnakeDirectionEnum;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class SnakeDirectionController {

    private final Playground playground;

    public SnakeDirectionController(Playground playground) {
        this.playground = playground;
    }

    // receive directions from player1
    @MessageMapping("/direction1")
    public void changeDirection1(SnakeDirectionEnum direction) {
        playground.getSnake1().setSnakeDirectionEnum(direction);
    }

    // receive directions from player2
    @MessageMapping("/direction2")
    public void changeDirection2(SnakeDirectionEnum direction) {
        playground.getSnake2().setSnakeDirectionEnum(direction);
    }

}
