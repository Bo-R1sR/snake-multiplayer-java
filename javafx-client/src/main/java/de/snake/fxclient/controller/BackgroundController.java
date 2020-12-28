package de.snake.fxclient.controller;

import de.snake.fxclient.logger.MyLogger;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Component
@FxmlView("background-stage.fxml")
public class BackgroundController {

    private final FxWeaver fxWeaver;
    private final MyLogger myLogger;

    @FXML
    StackPane viewHolder;

    public BackgroundController(FxWeaver fxWeaver, MyLogger myLogger) {
        this.fxWeaver = fxWeaver;
        this.myLogger = myLogger;
    }

    public StackPane getViewHolder() {
        return viewHolder;
    }

    @FXML
    public void initialize() {
        myLogger.log("Client started and ready");
        changeView(LoginController.class);
    }

    public void changeView(Class destinationClass) {
        Node view = fxWeaver.loadView(destinationClass);
        viewHolder.getChildren().setAll(view);
    }
}
