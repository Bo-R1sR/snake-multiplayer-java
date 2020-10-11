package de.snake.fxclient.controller;

import javafx.event.ActionEvent;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Controller
public class HistoryController {

    public void back(ActionEvent e1) {
        MainController mainController = MainController.getInstance();
        mainController.changeView("game");
    }

}
