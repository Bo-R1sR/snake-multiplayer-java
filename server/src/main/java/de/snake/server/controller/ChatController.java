package de.snake.server.controller;

import de.snake.server.domain.Message;
import de.snake.server.domain.OutputMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class ChatController {

    private int numberOfConnetion = 0;

    private final SimpMessagingTemplate template;

    public ChatController(SimpMessagingTemplate template) {
        this.template = template;
    }

    public int getNumberOfConnetion() {
        return numberOfConnetion;
    }

    public void setNumberOfConnetion(int numberOfConnetion) {
        this.numberOfConnetion = numberOfConnetion;
    }

//default /app/questions wird weitergeleitet auf topic/questions

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public OutputMessage send(Message message, SimpMessageHeaderAccessor headerAccessor) {

        //headerAccessor.getSessionAttributes().put("username", message.getFrom());
        Principal sender = headerAccessor.getUser();

        final String time = new SimpleDateFormat("HH:mm").format(new Date());
        return new OutputMessage(sender.getName(), message.getText(), time);
    }

    @MessageMapping("/playerId")
    @SendToUser(destinations="/queue/playerId", broadcast = false)
    public Integer sendID(Message message, SimpMessageHeaderAccessor headerAccessor) {
        System.out.println("hello");
        return ++numberOfConnetion;
    }

}
