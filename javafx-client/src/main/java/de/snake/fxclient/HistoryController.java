package de.snake.fxclient;

import de.snake.fxclient.BackgroundController;
import de.snake.fxclient.LogoutController;
import de.snake.fxclient.MainController;
import de.snake.fxclient.domain.User;
import de.snake.fxclient.task.HistoryTask;
import javafx.concurrent.WorkerStateEvent;
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
