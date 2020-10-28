package de.snake.server.game;

import java.io.Serializable;

public class SnakeBodyPart implements Serializable {
    private int positionX;
    private int PositionY;

    public SnakeBodyPart(int positionX, int PositionY) {
        this.positionX = positionX;
        this.PositionY = PositionY;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return PositionY;
    }

    public void setPositionY(int positionY) {
        this.PositionY = positionY;
    }

    public void decreaseX() {
        this.positionX--;
    }
    public void increaseX() {
        this.positionX++;
    }
    public void decreaseY() {
        this.PositionY--;
    }
    public void increaseY() {
        this.PositionY++;
    }
}
