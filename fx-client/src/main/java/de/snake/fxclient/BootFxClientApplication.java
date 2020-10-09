package de.snake.fxclient;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BootFxClientApplication {
    public static void main(String[] args) {
        Application.launch(FxClientApplication.class, args);
    }
}
