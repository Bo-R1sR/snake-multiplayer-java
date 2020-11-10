package de.snake.server.domain.game;

import java.io.Serializable;
import java.util.Random;


public class Food implements Serializable {
    Random rand = new Random();
    private int foodPositionX;
    private int foodPositionY;
    private int foodColor = 0;

    public Food(int foodPositionX, int foodPositionY) {
        this.foodPositionX = foodPositionX;
        this.foodPositionY = foodPositionY;
        int numberOfColors = 6;
        this.foodColor = rand.nextInt(numberOfColors);
    }


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
