package de.snake.fxclient.game;

import java.util.ArrayList;
import java.util.List;


public class Snake {

    private int lastFoodColor;
    private boolean immortal;

    private List<SnakeBodyPart> snakeBody = new ArrayList<>();

    public List<SnakeBodyPart> getSnakeBody() {
        return snakeBody;
    }

    public void setSnakeBody(List<SnakeBodyPart> snakeBody) {
        this.snakeBody = snakeBody;
    }

    public int getLastFoodColor() {
        return lastFoodColor;
    }

    public void setLastFoodColor(int lastFoodColor) {
        this.lastFoodColor = lastFoodColor;
    }

    public boolean isImmortal() {
        return immortal;
    }

    public void setImmortal(boolean immortal) {
        this.immortal = immortal;
    }
}
