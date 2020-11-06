package de.snake.fxclient;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

@SpringBootApplication
public class ClientApplication {

    // default ip
    private static String serverIp = "localhost";


    public static void main(String[] args) {
        // ip change possible


        //serverIp = args[0];
        Application.launch(JavaFxApplication.class, args);
    }

    @Bean
    public WebSocketStompClient webSocketStompClient() {
        return new WebSocketStompClient(new StandardWebSocketClient());
    }

}
