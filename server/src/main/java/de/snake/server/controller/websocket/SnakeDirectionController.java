package de.snake.server.controller.websocket;

import de.snake.server.domain.game.Playground;
import de.snake.server.domain.game.SnakeDirection;
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
    public void changeDirection1(SnakeDirection direction) {
        if (direction == SnakeDirection.UP && playground.getSnake1().getSnakeDirectionEnum() != SnakeDirection.DOWN)
            playground.getSnake1().setSnakeDirectionEnum(direction);
        if (direction == SnakeDirection.DOWN && playground.getSnake1().getSnakeDirectionEnum() != SnakeDirection.UP)
            playground.getSnake1().setSnakeDirectionEnum(direction);
        if (direction == SnakeDirection.RIGHT && playground.getSnake1().getSnakeDirectionEnum() != SnakeDirection.LEFT)
            playground.getSnake1().setSnakeDirectionEnum(direction);
        if (direction == SnakeDirection.LEFT && playground.getSnake1().getSnakeDirectionEnum() != SnakeDirection.RIGHT)
            playground.getSnake1().setSnakeDirectionEnum(direction);
    }

    // receive directions from player2
    @MessageMapping("/direction2")
    public void changeDirection2(SnakeDirection direction) {
//        playground.getSnake2().setSnakeDirectionEnum(direction);
        if (direction == SnakeDirection.UP && playground.getSnake2().getSnakeDirectionEnum() != SnakeDirection.DOWN)
            playground.getSnake2().setSnakeDirectionEnum(direction);
        if (direction == SnakeDirection.DOWN && playground.getSnake2().getSnakeDirectionEnum() != SnakeDirection.UP)
            playground.getSnake2().setSnakeDirectionEnum(direction);
        if (direction == SnakeDirection.RIGHT && playground.getSnake2().getSnakeDirectionEnum() != SnakeDirection.LEFT)
            playground.getSnake2().setSnakeDirectionEnum(direction);
        if (direction == SnakeDirection.LEFT && playground.getSnake2().getSnakeDirectionEnum() != SnakeDirection.RIGHT)
            playground.getSnake2().setSnakeDirectionEnum(direction);
    }

}
