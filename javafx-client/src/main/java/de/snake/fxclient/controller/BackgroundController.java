package de.snake.fxclient.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Component
@FxmlView("background-stage.fxml")
public class BackgroundController {

    private final FxWeaver fxWeaver;

    @FXML
    StackPane viewHolder;

    public BackgroundController(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }

    public StackPane getViewHolder() {
        return viewHolder;
    }

    public void setViewHolder(StackPane viewHolder) {
        this.viewHolder = viewHolder;
    }

    @FXML
    public void initialize() {
        changeView(LoginController.class);
    }

    public void changeView(Class destinationClass) {
        Node view = fxWeaver.loadView(destinationClass);
        viewHolder.getChildren().setAll(view);

    }
}
