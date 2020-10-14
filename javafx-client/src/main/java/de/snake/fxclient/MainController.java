package de.snake.fxclient;

import de.snake.fxclient.domain.User;
import de.snake.fxclient.task.HistoryTask;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Component
@FxmlView("main-stage.fxml")
public class MainController {

    private final BackgroundController backgroundController;
    private final User user;

    public MainController(BackgroundController backgroundController, User user) {
        this.backgroundController = backgroundController;
        this.user = user;
    }

    public void showHistory() {

        HistoryTask historyTask = new HistoryTask(user);
        new Thread(historyTask).start();

        historyTask.setOnSucceeded((WorkerStateEvent e2) -> {
            backgroundController.changeView(HistoryController.class);
        });

    }
    public void logout() {
        backgroundController.changeView(LogoutController.class);
    }

    public void startGame() {
    }

}
