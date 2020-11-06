package de.snake.fxclient.game.composite;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class Text extends Shape {
    private Point2D startPoint;
    private String value;
    private Paint color;

    public Text(GraphicsContext gc, Point2D startPoint, String value, Paint color) {
        super(gc);
        this.startPoint = startPoint;
        this.value = value;
        this.color = color;
    }

    @Override
    public void draw() {
        gc.setFill(color);
        gc.setFont(new Font("", 30));
        gc.fillText(value, startPoint.getX(), startPoint.getY());
    }
}
