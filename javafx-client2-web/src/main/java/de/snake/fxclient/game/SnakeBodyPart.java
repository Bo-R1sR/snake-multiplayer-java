package de.snake.fxclient.game;

public class SnakeBodyPart {
    private int PositionX;
    private int PositionY;
    private int color;

    public SnakeBodyPart(int PositionX, int PositionY, int color) {
        this.PositionX = PositionX;
        this.PositionY = PositionY;
        this.color = color;
    }

    public SnakeBodyPart() {

    }

    public int getPositionX() {
        return PositionX;
    }

    public void setPositionX(int positionX) {
        PositionX = positionX;
    }

    public int getPositionY() {
        return PositionY;
    }

    public void setPositionY(int positionY) {
        PositionY = positionY;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
