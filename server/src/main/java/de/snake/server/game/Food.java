package de.snake.server.game;

import java.io.Serializable;
import java.util.Random;

public class Food implements Serializable {
    private int foodX = 10;
    private int foodY = 10;
    private int foodColor = 0;


    public int getFoodX() {
        return foodX;
    }

    public void setFoodX(int foodX) {
        this.foodX = foodX;
    }

    public int getFoodY() {
        return foodY;
    }

    public void setFoodY(int foodY) {
        this.foodY = foodY;
    }

    public int getFoodColor() {
        return foodColor;
    }

    public void setFoodColor(int foodColor) {
        this.foodColor = foodColor;
    }
}
