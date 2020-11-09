package de.snake.server.domain.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Snake implements Serializable {

    private int lastFoodColor = 0;
    private boolean immortal = false;
    private int counter = 0;
    private int speed = 10;
    private int points = 0;

    private List<SnakeBodyPart> snakeBody = new ArrayList<>();

    public Snake(int numberBodyParts, int positionX, int positionY) {
        for (int i = 1; i <= numberBodyParts; i++) {
            snakeBody.add(new SnakeBodyPart(positionX, positionY, 0));
        }
    }

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

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
