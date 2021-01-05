package de.snake.fxclient.controller;

import de.snake.fxclient.domain.User;
import de.snake.fxclient.logger.MyLogger;
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

    private final User user;
    private final BackgroundController backgroundController;
    private final MyLogger myLogger;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Label loginFailure;

    @Value("${server.ip}")
    private String serverIp;

    public LoginController(User user, BackgroundController backgroundController, MyLogger myLogger) {
        this.user = user;
        this.backgroundController = backgroundController;
        this.myLogger = myLogger;
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
                loginFailure.setText("Username und/oder Passwort kann nicht leer sein.");
                loginFailure.setVisible(true);
                myLogger.log("Login Error");
            } else if (response == 200) {
                backgroundController.changeView(MainController.class);
                myLogger.log("Login Success");
            } else if (response == 403) {
                loginFailure.setText("Username und/oder Passwort sind falsch.");
                username.setText("");
                password.setText("");
                loginFailure.setVisible(true);
                myLogger.log("Login Error");
            } else {
                loginFailure.setText("Es gab einen unvorhersehbaren Fehler, bitte nochmal versuchen.");
                username.setText("");
                password.setText("");
                loginFailure.setVisible(true);
                myLogger.log("Login Error");
            }
        });
    }

    public void goToSignup() {
        backgroundController.changeView(SignupController.class);
    }
}
