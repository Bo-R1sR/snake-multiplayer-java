package de.snake.fxclient;

import de.snake.fxclient.game.Snake;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

@SpringBootApplication
public class ClientApplication {


    @Bean
    public WebSocketStompClient webSocketStompClient() {
        return new WebSocketStompClient(new StandardWebSocketClient());
    }

    @Bean
    public Snake snake1() {
        return new Snake();
    }

    @Bean
    public Snake snake2() {
        return new Snake();
    }


    public static void main(String[] args) {
        Application.launch(JavaFxApplication.class, args);
    }
}
