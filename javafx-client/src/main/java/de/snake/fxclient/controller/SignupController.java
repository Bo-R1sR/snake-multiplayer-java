package de.snake.fxclient.controller;

import de.snake.fxclient.domain.User;
import de.snake.fxclient.task.SignupTask;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Component
@FxmlView("signup-stage.fxml")
public class SignupController {

    private final User user;
    private final BackgroundController backgroundController;

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField confirmPassword;
    @FXML
    private Label signupFailure;

    public SignupController(User user, BackgroundController backgroundController) {
        this.user = user;
        this.backgroundController = backgroundController;
    }

    public void signupAction() {
        user.setName(username.getText());
        user.setPassword(password.getText());
        user.setConfirmPassword(confirmPassword.getText());

        SignupTask signupTask = new SignupTask(user);
        new Thread(signupTask).start();

        signupTask.setOnSucceeded((WorkerStateEvent e2) -> {

            signupFailure.setVisible(false);

            Integer response = signupTask.getValue();

            if (response == -2) {
                signupFailure.setText("username and/or password can not be empty");
                signupFailure.setVisible(true);
            } else if (response == -1) {
                signupFailure.setText("passwords do not match");
                password.setText("");
                confirmPassword.setText("");
                signupFailure.setVisible(true);
            } else if (response == 400) {
                signupFailure.setText("username " + user.getName() + " already taken");
                username.setText("");
                signupFailure.setVisible(true);
            } else if (response == 200) {
                user.reset();
                backgroundController.changeView(LoginController.class);
            } else {
                signupFailure.setText("there was an unexpected error, please try again");
                username.setText("");
                password.setText("");
                confirmPassword.setText("");
                signupFailure.setVisible(true);
            }
        });
    }

    public void backToLoginPage() {
        backgroundController.changeView(LoginController.class);
    }
}
