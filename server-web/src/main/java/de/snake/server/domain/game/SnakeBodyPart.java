package de.snake.server.domain.game;

import java.io.Serializable;

public class SnakeBodyPart implements Serializable {
    private int PositionX;
    private int PositionY;
    private int color;

    public SnakeBodyPart(int positionX, int PositionY, int color) {
        this.PositionX = positionX;
        this.PositionY = PositionY;
        this.color = color;
    }

    public int getPositionX() {
        return PositionX;
    }

    public void setPositionX(int positionX) {
        this.PositionX = positionX;
    }

    public int getPositionY() {
        return PositionY;
    }

    public void setPositionY(int positionY) {
        this.PositionY = positionY;
    }

    public void decreaseX() {
        this.PositionX--;
    }

    public void increaseX() {
        this.PositionX++;
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
