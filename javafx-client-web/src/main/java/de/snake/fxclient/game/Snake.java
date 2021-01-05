package de.snake.fxclient.game;

import java.util.ArrayList;
import java.util.List;


public class Snake {

    private int lastFoodColor;
    private boolean immortal;
    private boolean possibleToBite = false;
    private int speed;
    private int points;

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

    public boolean isPossibleToBite() {
        return possibleToBite;
    }

    public void setPossibleToBite(boolean possibleToBite) {
        this.possibleToBite = possibleToBite;
    }

}
