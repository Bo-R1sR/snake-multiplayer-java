package de.snake.fxclient.game;

import java.io.Serializable;

public class Food implements Serializable {
    private int foodPositionX;
    private int foodPositionY;
    private int foodColor;

    public int getFoodPositionX() {
        return foodPositionX;
    }

    public void setFoodPositionX(int foodPositionX) {
        this.foodPositionX = foodPositionX;
    }

    public int getFoodPositionY() {
        return foodPositionY;
    }

    public void setFoodPositionY(int foodPositionY) {
        this.foodPositionY = foodPositionY;
    }

    public int getFoodColor() {
        return foodColor;
    }

    public void setFoodColor(int foodColor) {
        this.foodColor = foodColor;
    }
}
