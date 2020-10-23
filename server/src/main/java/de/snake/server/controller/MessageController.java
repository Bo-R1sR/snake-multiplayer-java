package de.snake.server.controller;

import de.snake.server.domain.Message;
import de.snake.server.domain.OutputMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class MessageController {
    //default /app/questions wird weitergeleitet auf topic/questions

    @MessageMapping("/game")
    @SendTo("/topic/messages")
    public OutputMessage send(Message message, SimpMessageHeaderAccessor headerAccessor) {

        //headerAccessor.getSessionAttributes().put("username", message.getFrom());
        Principal sender = headerAccessor.getUser();

        final String time = new SimpleDateFormat("HH:mm").format(new Date());
        return new OutputMessage(sender.getName(), message.getText(), time);
    }

}
