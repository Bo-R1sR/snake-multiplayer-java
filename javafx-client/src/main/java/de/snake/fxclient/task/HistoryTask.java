package de.snake.fxclient.task;

import de.snake.fxclient.domain.User;
import javafx.concurrent.Task;

public class HistoryTask extends Task<Integer> {

    private final User user;

    public HistoryTask(User user) {
        this.user = user;
    }

    @Override
    protected Integer call() {

        return null;
    }
}

