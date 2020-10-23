package de.snake.server.game;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
public class Snake implements Serializable {
    private int width;
    private int height;
    private List<SnakeBodyPart> snakeBody;

    @PostConstruct
    public void init() {
        width = 20;
        height = 20;
        snakeBody = new ArrayList<>();
        snakeBody.add(new SnakeBodyPart(width / 2 , height / 2));
        snakeBody.add(new SnakeBodyPart(width / 2 , height / 2));
        snakeBody.add(new SnakeBodyPart(width / 2 , height / 2));
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<SnakeBodyPart> getSnakeBody() {
        return snakeBody;
    }

    public void setSnakeBody(List<SnakeBodyPart> snakeBody) {
        this.snakeBody = snakeBody;
    }
}
