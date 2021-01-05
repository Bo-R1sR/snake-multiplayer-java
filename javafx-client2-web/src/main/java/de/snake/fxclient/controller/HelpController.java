package de.snake.fxclient.controller;

import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Component
@FxmlView("help-stage.fxml")
public class HelpController {
    private final BackgroundController backgroundController;

    public HelpController(BackgroundController backgroundController) {
        this.backgroundController = backgroundController;
    }

    public void back() {
        backgroundController.changeView(MainController.class);
    }
}
