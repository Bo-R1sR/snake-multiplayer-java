package de.snake.fxclient.game;

public class SnakeBodyPart {
    private int PositionX;
    private int PositionY;

    public SnakeBodyPart(int PositionX, int PositionY) {
        this.PositionX = PositionX;
        this.PositionY = PositionY;
    }

    public SnakeBodyPart() {
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
}
