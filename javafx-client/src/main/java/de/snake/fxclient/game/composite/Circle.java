package de.snake.fxclient.game.composite;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Circle extends Shape {
    private final Point2D startPoint;
    private final double radius;
    private Paint backgroundColor;

    public Circle(GraphicsContext gc, int backgroundColorNumber, Point2D startPoint, double radius) {
        super(gc);
        this.startPoint = startPoint;
        this.radius = radius;
        switch (backgroundColorNumber) {
            case 0 -> backgroundColor = Color.PURPLE;
            case 1 -> backgroundColor = Color.LIGHTBLUE;
            case 2 -> backgroundColor = Color.YELLOW;
            case 3 -> backgroundColor = Color.RED;
            case 4 -> backgroundColor = Color.PINK;
            case 5 -> backgroundColor = Color.DARKGREEN;
        }
    }

    @Override
    public void draw() {
        gc.setFill(backgroundColor);
        gc.fillOval(startPoint.getX(), startPoint.getY(), radius, radius);
    }
}
