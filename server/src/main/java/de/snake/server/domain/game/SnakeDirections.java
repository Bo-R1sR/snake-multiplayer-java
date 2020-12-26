package de.snake.server.domain.game;

import org.springframework.stereotype.Component;

@Component
public class SnakeDirections {
    private SnakeDirectionEnum direction1;
    private SnakeDirectionEnum direction2;

    public SnakeDirectionEnum getDirection1() {
        return direction1;
    }

    public void setDirection1(SnakeDirectionEnum direction1) {
        this.direction1 = direction1;
    }

    public SnakeDirectionEnum getDirection2() {
        return direction2;
    }

    public void setDirection2(SnakeDirectionEnum direction2) {
        this.direction2 = direction2;
    }
}
