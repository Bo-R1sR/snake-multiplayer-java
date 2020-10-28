package de.snake.server.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Snake implements Serializable {

    private List<SnakeBodyPart> snakeBody = new ArrayList<>();

    public Snake(int numberBodyParts, int positionX, int positionY) {
        for (int i = 1; i <= numberBodyParts; i++) {
            snakeBody.add(new SnakeBodyPart(positionX, positionY));
        }
    }

    public List<SnakeBodyPart> getSnakeBody() {
        return snakeBody;
    }

    public void setSnakeBody(List<SnakeBodyPart> snakeBody) {
        this.snakeBody = snakeBody;
    }
}
