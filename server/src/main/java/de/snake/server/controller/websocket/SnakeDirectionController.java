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
        if (direction == SnakeDirection.UP && playground.getSnake1().getMoveDirection() != SnakeDirection.DOWN)
            playground.getSnake1().setSnakeDirection(direction);
        if (direction == SnakeDirection.DOWN && playground.getSnake1().getMoveDirection() != SnakeDirection.UP)
            playground.getSnake1().setSnakeDirection(direction);
        if (direction == SnakeDirection.RIGHT && playground.getSnake1().getMoveDirection() != SnakeDirection.LEFT)
            playground.getSnake1().setSnakeDirection(direction);
        if (direction == SnakeDirection.LEFT && playground.getSnake1().getMoveDirection() != SnakeDirection.RIGHT)
            playground.getSnake1().setSnakeDirection(direction);
    }

    // receive directions from player2
    @MessageMapping("/direction2")
    public void changeDirection2(SnakeDirection direction) {
        if (direction == SnakeDirection.UP && playground.getSnake2().getMoveDirection() != SnakeDirection.DOWN)
            playground.getSnake2().setSnakeDirection(direction);
        if (direction == SnakeDirection.DOWN && playground.getSnake2().getMoveDirection() != SnakeDirection.UP)
            playground.getSnake2().setSnakeDirection(direction);
        if (direction == SnakeDirection.RIGHT && playground.getSnake2().getMoveDirection() != SnakeDirection.LEFT)
            playground.getSnake2().setSnakeDirection(direction);
        if (direction == SnakeDirection.LEFT && playground.getSnake2().getMoveDirection() != SnakeDirection.RIGHT)
            playground.getSnake2().setSnakeDirection(direction);
    }

}
