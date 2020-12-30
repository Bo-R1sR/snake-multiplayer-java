package de.snake.fxclient.controller;

import de.snake.fxclient.domain.User;
import de.snake.fxclient.logger.MyLogger;
import de.snake.fxclient.websocket.CustomStompClient;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@FxmlView("main-stage.fxml")
public class MainController {

    private final BackgroundController backgroundController;
    private final User user;
    private final CustomStompClient customStompClient;
    private final MyLogger myLogger;

    @Value("${server.ip}")
    private String serverIp;

    public MainController(BackgroundController backgroundController, User user, CustomStompClient customStompClient, MyLogger myLogger) {
        this.backgroundController = backgroundController;
        this.user = user;
        this.customStompClient = customStompClient;
        this.myLogger = myLogger;
    }


    public void showHistory() {
        backgroundController.changeView(HistoryController.class);
    }

    public void logout() {
        user.reset();
        backgroundController.changeView(LoginController.class);
    }

    public void showGame() {
        backgroundController.changeView(GameController.class);
        customStompClient.connectToServerWS();
        myLogger.log("Establishing WebSocket Connection");
    }
}
