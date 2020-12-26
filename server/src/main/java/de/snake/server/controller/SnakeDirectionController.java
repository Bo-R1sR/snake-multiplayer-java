package de.snake.server.controller;

import de.snake.server.domain.game.SnakeDirections;
import de.snake.server.domain.game.SnakeDirectionEnum;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class SnakeDirectionController {

private final SnakeDirections snakeDirections;

    public SnakeDirectionController(SnakeDirections snakeDirections) {
        this.snakeDirections = snakeDirections;
    }

    // receive directions from player1
    @MessageMapping("/direction1")
    public void changeDirection1(SnakeDirectionEnum direction) {
        snakeDirections.setDirection1(direction);
    }

    // receive directions from player2
    @MessageMapping("/direction2")
    public void changeDirection2(SnakeDirectionEnum direction) {
        snakeDirections.setDirection2(direction);
    }

}
