package de.snake.fxclient.game.composite;

import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class Text extends Shape {
    private Point2D startPoint;
    private String value;
    private Paint color;
    private boolean center;

    public Text(GraphicsContext gc, Point2D startPoint, String value, Paint color, boolean center) {
        super(gc);
        this.startPoint = startPoint;
        this.value = value;
        this.color = color;
        this.center = center;
    }

    @Override
    public void draw() {
        gc.setFill(color);
        gc.setFont(new Font("", 30));
        if (center) {
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.CENTER);
            gc.fillText(value, Math.round(gc.getCanvas().getWidth()  / 2), Math.round(gc.getCanvas().getHeight() / 2));
        }
        else {
            gc.fillText(value, startPoint.getX(), startPoint.getY());
        }


    }
}
