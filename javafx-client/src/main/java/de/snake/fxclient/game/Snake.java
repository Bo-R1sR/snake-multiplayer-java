package de.snake.fxclient.game;

import java.util.ArrayList;
import java.util.List;


public class Snake {
    private List<SnakeBodyPart> snakeBody = new ArrayList<>();

    public List<SnakeBodyPart> getSnakeBody() {
        return snakeBody;
    }

    public void setSnakeBody(List<SnakeBodyPart> snakeBody) {
        this.snakeBody = snakeBody;
    }
}
