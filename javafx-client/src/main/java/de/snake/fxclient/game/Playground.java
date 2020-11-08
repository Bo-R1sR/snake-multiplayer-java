package de.snake.fxclient.game;

import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class Playground implements Serializable {

    private int width = 25;
    private int height = 25;
    private int snakeBodySize = 20;
    private Snake snake1;
    private Snake snake2;
    private int levelNumber;
    private Food food;
    private boolean gameOver;

    public Playground() {
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getSnakeBodySize() {
        return snakeBodySize;
    }

    public void setSnakeBodySize(int snakeBodySize) {
        this.snakeBodySize = snakeBodySize;
    }

    public Snake getSnake1() {
        return snake1;
    }

    public void setSnake1(Snake snake1) {
        this.snake1 = snake1;
    }

    public Snake getSnake2() {
        return snake2;
    }

    public void setSnake2(Snake snake2) {
        this.snake2 = snake2;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }
}
