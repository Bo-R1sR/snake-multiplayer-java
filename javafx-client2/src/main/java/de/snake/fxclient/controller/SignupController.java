package de.snake.fxclient.controller;

import de.snake.fxclient.domain.User;
import de.snake.fxclient.logger.MyLogger;
import de.snake.fxclient.task.SignupTask;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@FxmlView("signup-stage.fxml")
public class SignupController {

    private final User user;
    private final BackgroundController backgroundController;
    private final MyLogger myLogger;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private Label signupFailure;

    @Value("${server.ip}")
    private String serverIp;

    public SignupController(User user, BackgroundController backgroundController, MyLogger myLogger) {
        this.user = user;
        this.backgroundController = backgroundController;
        this.myLogger = myLogger;
    }

    public void signupAction() {
        user.setName(username.getText());
        user.setPassword(password.getText());
        user.setConfirmPassword(confirmPassword.getText());

        SignupTask signupTask = new SignupTask(user, serverIp);
        new Thread(signupTask).start();

        signupTask.setOnSucceeded((WorkerStateEvent e2) -> {

            signupFailure.setVisible(false);

            Integer response = signupTask.getValue();

            if (response == -2) {
                signupFailure.setText("Username und/oder Passwort können nicht leer sein.");
                signupFailure.setVisible(true);
                myLogger.log("Signup Error");
            } else if (response == -1) {
                signupFailure.setText("Passwörter stimmen nicht überein");
                password.setText("");
                confirmPassword.setText("");
                signupFailure.setVisible(true);
                myLogger.log("Signup Error");
            } else if (response == 400) {
                signupFailure.setText("Username " + user.getName() + " schon vergeben.");
                username.setText("");
                signupFailure.setVisible(true);
                myLogger.log("Signup Error");
            } else if (response == 200) {
                user.reset();
                backgroundController.changeView(LoginController.class);
                myLogger.log("Signup Success");
            } else {
                signupFailure.setText("Es gab einen unvorhersehbaren Fehler, bitte nochmal versuchen.");
                username.setText("");
                password.setText("");
                confirmPassword.setText("");
                signupFailure.setVisible(true);
                myLogger.log("Signup Error");
            }
        });
    }

    public void backToLoginPage() {
        backgroundController.changeView(LoginController.class);
    }
}
