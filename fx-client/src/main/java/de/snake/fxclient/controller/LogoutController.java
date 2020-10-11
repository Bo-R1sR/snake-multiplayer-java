package de.snake.fxclient.controller;

import de.snake.fxclient.task.HistoryTask;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import org.springframework.stereotype.Controller;

@Controller
public class LogoutController {

//    @FXML
//    public void initialize() {
//        // executed when the controller object is created by the framework
//    }

    public void goToLoginPage(ActionEvent e1) {
        // execute login request in async task

        MainController mainController = MainController.getInstance();




            MainController.getInstance().changeView("login");


    }


}

