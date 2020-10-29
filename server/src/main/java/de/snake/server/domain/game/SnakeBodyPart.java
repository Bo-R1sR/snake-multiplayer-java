package de.snake.server.domain.game;

import java.io.Serializable;

public class SnakeBodyPart implements Serializable {
    private int positionX;
    private int PositionY;
    private int color;

    public SnakeBodyPart(int positionX, int PositionY, int color) {
        this.positionX = positionX;
        this.PositionY = PositionY;
        this.color = color;
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

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
