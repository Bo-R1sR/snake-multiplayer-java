package de.snake.fxclient.game.composite;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class CompositeShape extends Shape {
    //Set<Shape> components = new HashSet<>();
    private List<Shape> components = new ArrayList<>();

    public CompositeShape(GraphicsContext gc, List<Shape> components) {
        //public CompositeShape(GraphicsContext gc, Set<Shape> components) {
        super(gc);
        this.components = components;
    }

    @Override
    public void draw() {
        components.forEach(Shape::draw);
    }
}