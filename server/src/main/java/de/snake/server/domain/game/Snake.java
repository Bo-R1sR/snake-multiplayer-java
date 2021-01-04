package de.snake.server.domain.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Snake implements Serializable {

    private int lastFoodColor = 0;
    private boolean immortal = false;
    private boolean possibleToBite = false;
    private int counter = 0;
    private int speed = 10;
    private int points = 0;
    private SnakeDirection snakeDirection;
    private SnakeDirection moveDirection;
    private String username;

    private List<SnakeBodyPart> snakeBody = new ArrayList<>();

    public Snake(int numberBodyParts, int positionX, int positionY) {
        for (int i = 1; i <= numberBodyParts; i++) {
            snakeBody.add(new SnakeBodyPart(positionX, positionY, 0));
        }
    }

    public void resetCounter() {
        setCounter(0);
    }

    public void increaseCounter() {
        setCounter(getCounter() + 1);
    }

    public SnakeBodyPart getHead() {
        return snakeBody.get(0);
    }

    public void increasePoints() {
        setPoints(getPoints() + 1);
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

    public SnakeDirection getSnakeDirection() {
        return snakeDirection;
    }

    public void setSnakeDirection(SnakeDirection snakeDirection) {
        this.snakeDirection = snakeDirection;
    }

    public boolean isPossibleToBite() {
        return possibleToBite;
    }

    public void setPossibleToBite(boolean possibleToBite) {
        this.possibleToBite = possibleToBite;
    }

    public SnakeDirection getMoveDirection() {
        return moveDirection;
    }

    public void setMoveDirection(SnakeDirection moveDirection) {
        this.moveDirection = moveDirection;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
