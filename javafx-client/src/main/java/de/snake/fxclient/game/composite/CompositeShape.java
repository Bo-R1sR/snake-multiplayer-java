package de.snake.fxclient.game.composite;

import javafx.scene.canvas.GraphicsContext;

import java.util.List;

public class CompositeShape extends Shape {
    private List<Shape> components;

    public CompositeShape(GraphicsContext gc, List<Shape> components) {
        super(gc);
        this.components = components;
    }

    @Override
    public void draw() {
        components.forEach(Shape::draw);
    }
}