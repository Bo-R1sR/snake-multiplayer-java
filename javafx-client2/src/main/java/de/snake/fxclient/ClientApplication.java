package de.snake.fxclient;

import de.snake.fxclient.logger.LoggerFactory;
import de.snake.fxclient.logger.MyLogger;
import javafx.application.Application;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

@SpringBootApplication
public class ClientApplication {

    @Value("${log.type}")
    private String type;

    public static void main(String[] args) {
        Application.launch(JavaFxApplication.class, args);
    }

    @Bean
    public WebSocketStompClient webSocketStompClient() {
        return new WebSocketStompClient(new StandardWebSocketClient());
    }

    @Bean
    public MyLogger createLogger() {
        if (type.equals("file")) {
            return LoggerFactory.createLogger(LoggerFactory.LogType.FILE);
        } else if (type.equals("console")) {
            return LoggerFactory.createLogger(LoggerFactory.LogType.CONSOLE);
        } else return LoggerFactory.createLogger();
    }

}
