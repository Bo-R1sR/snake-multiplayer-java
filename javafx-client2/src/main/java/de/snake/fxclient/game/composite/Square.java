package de.snake.fxclient.game.composite;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class Square extends Shape {
    private final Paint backgroundColor;
    private final Point2D startPoint;
    private final double width;
    private final double height;

    public Square(GraphicsContext gc, Paint backgroundColor, Point2D startPoint, double width, double height) {
        super(gc);
        this.backgroundColor = backgroundColor;
        this.startPoint = startPoint;
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw() {
        gc.setFill(backgroundColor);
        gc.fillRect(startPoint.getX(), startPoint.getY(), width, height);
    }
}
