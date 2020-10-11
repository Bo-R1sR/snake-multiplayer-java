package de.snake.fxclient.controller;

import com.sun.tools.javac.Main;
import de.snake.fxclient.domain.User;
import de.snake.fxclient.task.LoginTask;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

@Controller
public class LoginController {

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Label loginFailure;

//    @FXML
//    public void initialize() {
//    }

    public void loginAction(ActionEvent e1) {
        // execute login request in async task
        LoginTask loginTask = new LoginTask(username.getText(), password.getText());
        new Thread(loginTask).start();

        // login task response handler
        loginTask.setOnSucceeded((WorkerStateEvent e2) -> {
            // login is successfull if the return value is not null
            if (loginTask.getValue() == null) {
                loginFailure.setVisible(true);
                return;
            }
            // navigate to catalog view

            User loggedInUser = loginTask.getValue();

            MainController mainController = MainController.getInstance();

            mainController.setJsonWebToken(loggedInUser.getJsonWebToken());
            mainController.setUsername(username.getText());

            mainController.changeView("game");

        });
    }

    public void goToSignup(ActionEvent e1) {
        MainController mainController = MainController.getInstance();
        mainController.changeView("signup");
    }



}
