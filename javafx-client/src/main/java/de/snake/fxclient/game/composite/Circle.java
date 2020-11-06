package de.snake.fxclient.game.composite;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Circle extends Shape {
    private int backgroundColorNumber;
    private Point2D startPoint;
    private double radius;
    private Paint backgroundColor;

    public Circle(GraphicsContext gc, int backgroundColorNumber, Point2D startPoint, double radius) {
        super(gc);
        this.backgroundColorNumber = backgroundColorNumber;
        this.startPoint = startPoint;
        this.radius = radius;
        switch (backgroundColorNumber) {
            case 0:
                backgroundColor = Color.PURPLE;
                break;
            case 1:
                backgroundColor = Color.LIGHTBLUE;
                break;
            case 2:
                backgroundColor = Color.YELLOW;
                break;
            case 3:
                backgroundColor = Color.RED;
                break;
            case 4:
                backgroundColor = Color.PINK;
                break;
            case 5:
                backgroundColor = Color.DARKGREEN;
                break;
        }


    }

    @Override
    public void draw() {
        gc.setFill(backgroundColor);
        gc.fillOval(startPoint.getX(), startPoint.getY(), radius, radius);
    }
}
