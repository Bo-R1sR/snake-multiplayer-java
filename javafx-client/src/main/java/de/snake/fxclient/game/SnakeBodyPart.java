package de.snake.fxclient.game;

public class SnakeBodyPart {
    private int x;
    private int y;

    public SnakeBodyPart(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public SnakeBodyPart() {
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
