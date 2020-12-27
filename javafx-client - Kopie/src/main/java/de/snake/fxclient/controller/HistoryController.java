package de.snake.fxclient.controller;

import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Component
@FxmlView("history-stage.fxml")
public class HistoryController {

    private final BackgroundController backgroundController;

    public HistoryController(BackgroundController backgroundController) {
        this.backgroundController = backgroundController;
    }

    public void back() {
        backgroundController.changeView(MainController.class);
    }

}
