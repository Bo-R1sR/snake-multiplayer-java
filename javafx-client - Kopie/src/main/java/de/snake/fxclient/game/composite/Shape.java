package de.snake.fxclient.game.composite;

import javafx.scene.canvas.GraphicsContext;

public abstract class Shape {
    GraphicsContext gc;

    public Shape(GraphicsContext gc) {
        this.gc = gc;
    }

    public abstract void draw();
}