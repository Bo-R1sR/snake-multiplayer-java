package de.snake.fxclient.game;

import java.io.Serializable;

public class Food implements Serializable {
    private int foodX;
    private int foodY;
    private int foodColor;

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
