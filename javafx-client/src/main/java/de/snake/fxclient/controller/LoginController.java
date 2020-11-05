package de.snake.fxclient.controller;

import de.snake.fxclient.domain.User;
import de.snake.fxclient.task.LoginTask;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@FxmlView("login-stage.fxml")
public class LoginController {

    @Value("${server.ip}")
    private String serverIp;

    private final User user;
    private final BackgroundController backgroundController;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Label loginFailure;

    public LoginController(User user, BackgroundController backgroundController) {
        this.user = user;
        this.backgroundController = backgroundController;
    }

    public void loginAction() {
        user.setName(username.getText());
        user.setPassword(password.getText());

         LoginTask loginTask = new LoginTask(user, serverIp);
        new Thread(loginTask).start();

        loginTask.setOnSucceeded((WorkerStateEvent e2) -> {

            loginFailure.setVisible(false);

            Integer response = loginTask.getValue();

            if (response == -2) {
                loginFailure.setText("username and/or password can not be empty");
                loginFailure.setVisible(true);
            } else if (response == 200) {
                backgroundController.changeView(MainController.class);
            } else if (response == 403) {
                loginFailure.setText("username and/or password wrong");
                username.setText("");
                password.setText("");
                loginFailure.setVisible(true);
            } else {
                loginFailure.setText("there was an unexpected error, please try again");
                username.setText("");
                password.setText("");
                loginFailure.setVisible(true);
            }
        });
    }

    public void goToSignup() {
        backgroundController.changeView(SignupController.class);
    }
}
