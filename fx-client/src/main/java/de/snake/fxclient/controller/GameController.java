package de.snake.fxclient.controller;

import de.snake.fxclient.task.HistoryTask;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {

//    @FXML
//    public void initialize() {
//        // executed when the controller object is created by the framework
//    }

    public void showHistory(ActionEvent e1) {
        // execute login request in async task

        MainController mainController = MainController.getInstance();

        String token = mainController.getJsonWebToken();
        String username = mainController.getUsername();

        HistoryTask historyTask = new HistoryTask(token, username);
        new Thread(historyTask).start();

        // login task response handler
        historyTask.setOnSucceeded((WorkerStateEvent e2) -> {
            // login is successfull if the return value is not null
//            if (loginTask.getValue() == null) {
//                loginFailure.setVisible(true);
//                return;
//            }


            MainController.getInstance().changeView("history");
        });

    }
    public void logout(ActionEvent e1) {
        MainController.getInstance().changeView("logout");


    }

    public void startGame(ActionEvent e1) {


    }

}

