package de.snake.fxclient.game;

import javafx.scene.paint.Color;
import org.springframework.stereotype.Component;

@Component
public class SnakeColor {

    private Color colorSnake1 = Color.BLUE;
    private Color colorSnake1Border = Color.LIGHTBLUE;
    private Color colorSnake2 = Color.GREEN;
    private Color colorSnake2Border = Color.LIGHTGREEN;

    public Color getColorSnake1() {
        return colorSnake1;
    }

    public void setColorSnake1(Color colorSnake1) {
        this.colorSnake1 = colorSnake1;
    }

    public Color getColorSnake1Border() {
        return colorSnake1Border;
    }

    public void setColorSnake1Border(Color colorSnake1Border) {
        this.colorSnake1Border = colorSnake1Border;
    }

    public Color getColorSnake2() {
        return colorSnake2;
    }

    public void setColorSnake2(Color colorSnake2) {
        this.colorSnake2 = colorSnake2;
    }

    public Color getColorSnake2Border() {
        return colorSnake2Border;
    }

    public void setColorSnake2Border(Color colorSnake2Border) {
        this.colorSnake2Border = colorSnake2Border;
    }
}
