package de.snake.server.controller.websocket;

import de.snake.server.domain.Message;
import de.snake.server.domain.OutputMessage;
import de.snake.server.domain.game.ScreenText;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class ChatController {
    private final SimpMessagingTemplate template;
    private final ScreenText screenText;

    public ChatController(SimpMessagingTemplate template, ScreenText screenText) {
        this.template = template;
        this.screenText = screenText;
    }

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public OutputMessage send(Message message) {
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        // send screen text again when player connects to see actual status
        if (message.getFrom().equals("SYSTEM")) {
            template.convertAndSend("/topic/screenText", screenText);
        }
        return new OutputMessage(message.getFrom(), message.getText(), time);
    }
}
