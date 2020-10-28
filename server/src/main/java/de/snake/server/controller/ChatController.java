package de.snake.server.controller;

import de.snake.server.domain.Message;
import de.snake.server.domain.OutputMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class ChatController {

    private int numberOfConnections = 0;


    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public OutputMessage send(Message message, SimpMessageHeaderAccessor headerAccessor) {
        Principal sender = headerAccessor.getUser();
        final String time = new SimpleDateFormat("HH:mm").format(new Date());
        return new OutputMessage(sender.getName(), message.getText(), time);
    }

    @MessageMapping("/playerId")
    @SendToUser(destinations = "/queue/playerId", broadcast = false)
    public Integer sendID() {
        return ++numberOfConnections;
    }

}
