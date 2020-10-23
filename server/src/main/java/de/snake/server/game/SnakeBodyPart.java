package de.snake.server.game;

import java.io.Serializable;

public class SnakeBodyPart implements Serializable {
    private int x;
    private int y;

    public SnakeBodyPart(int x, int y) {
        this.x = x;
        this.y = y;
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

    public void decreaseX() {
        this.x--;
    }
    public void increaseX() {
        this.x++;
    }
    public void decreaseY() {
        this.y--;
    }
    public void increaseY() {
        this.y++;
    }
}
