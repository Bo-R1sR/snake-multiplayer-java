package de.snake.fxclient.controller;

import com.jfoenix.controls.JFXTreeTableView;
import de.snake.fxclient.domain.User;
import de.snake.fxclient.task.HistoryTask;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import net.rgielen.fxweaver.core.FxmlView;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
@FxmlView("history-stage.fxml")
public class HistoryController {

    @Value("${server.ip}")
    private String serverIp;

    @FXML
    private JFXTreeTableView tableView;

    private final BackgroundController backgroundController;
    private final User user;

    private String responseBody;
    private String formattedResponseBody;

    public HistoryController(BackgroundController backgroundController, User user) {
        this.backgroundController = backgroundController;
        this.user = user;
    }

    public void initialize(){
        HistoryTask historyTask = new HistoryTask(user, serverIp);
        new Thread(historyTask).start();

        historyTask.setOnSucceeded((WorkerStateEvent e2) -> {
            responseBody = historyTask.getResponseBody();
            formattedResponseBody = responseBody.substring(1,responseBody.length()-1);
            System.out.println(formattedResponseBody);
            JSONObject object = new JSONObject(formattedResponseBody);
            for (String keyStr : object.keySet()){
                Object keyvalue = object.get(keyStr);
                System.out.println("key: " + keyStr + " value: " + keyvalue);
            }
        });

    }
    public void back() {
        backgroundController.changeView(MainController.class);
    }

}
