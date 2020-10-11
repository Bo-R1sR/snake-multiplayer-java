package de.snake.fxclient.controller;

import de.snake.fxclient.task.SignupTask;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Controller;

@Controller
public class SignupController {

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private Label signupFailure;

    @FXML
    private Label signupSuccess;
//
//    @FXML
//    public void initialize() {
//    }

    public void signupAction(ActionEvent e1) {
        if (!password.getText().equals(confirmPassword.getText())) {
            signupFailure.setVisible(true);
            return;
        } else {
            signupFailure.setVisible(false);
        }

        // execute login request in async task
        SignupTask signupTask = new SignupTask(username.getText(), password.getText());
        new Thread(signupTask).start();

        // login task response handler
        signupTask.setOnSucceeded((WorkerStateEvent e2) -> {
            // login is successfull if the return value is not null
            if (signupTask.getValue() == null) {
                signupFailure.setVisible(true);
                //signupFailure.setText();
                return;
            }
            signupSuccess.setVisible(true);


            MainController.getInstance().changeView("login");

        });
    }

    public void backToLoginPage(ActionEvent e1) {
        MainController.getInstance().changeView("login");
    }
}
