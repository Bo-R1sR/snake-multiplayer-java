package de.snake.server.game;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;

@Component
public class Playground implements Serializable {
//
//    private final Snake snake1;
//    private final Snake snake2;
//
//
//    public Playground(Snake snake1, Snake snake2) {
//        this.snake1 = snake1;
//        this.snake2 = snake2;
//    }
//
//    public Snake getSnake1() {
//        return snake1;
//    }
//
//    public Snake getSnake2() {
//        return snake2;
//    }
private Snake snake1;

    private Snake snake2;

    private Food food;

    private boolean gameOver;

    public Playground(){

    }

    @PostConstruct
    public void init() {
        snake1 = new Snake(11);
        snake2 = new Snake(9);
        food = new Food();
        gameOver = false;
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
}
