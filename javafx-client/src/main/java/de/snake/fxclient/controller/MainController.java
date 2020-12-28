package de.snake.fxclient.controller;

import de.snake.fxclient.domain.User;
import de.snake.fxclient.task.HistoryTask;
import de.snake.fxclient.websocket.CustomStompClient;
import javafx.concurrent.WorkerStateEvent;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@FxmlView("main-stage.fxml")
public class MainController {

    private final BackgroundController backgroundController;
    private final User user;
    private final CustomStompClient customStompClient;

    @Value("${server.ip}")
    private String serverIp;

    public MainController(BackgroundController backgroundController, User user, CustomStompClient customStompClient) {
        this.backgroundController = backgroundController;
        this.user = user;
        this.customStompClient = customStompClient;
    }


    public void showHistory() {

        HistoryTask historyTask = new HistoryTask(user, serverIp);
        new Thread(historyTask).start();

        historyTask.setOnSucceeded((WorkerStateEvent e2) -> {
            backgroundController.changeView(HistoryController.class);
        });

    }

    public void logout() {
        user.reset();
        backgroundController.changeView(LoginController.class);
    }

    public void showGame() {
        backgroundController.changeView(GameController.class);
        customStompClient.connectToServerWS();
    }
}
