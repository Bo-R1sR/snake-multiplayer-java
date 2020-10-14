package de.snake.fxclient.controller;

import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Component
@FxmlView("logout-stage.fxml")
public class LogoutController {
    private final BackgroundController backgroundController;

    public LogoutController(BackgroundController backgroundController) {
        this.backgroundController = backgroundController;
    }

    public void goToLoginPage() {
        backgroundController.changeView(LoginController.class);
    }
}


