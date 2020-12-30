package de.snake.fxclient.websocket;

import de.snake.fxclient.controller.GameController;
import de.snake.fxclient.domain.User;
import de.snake.fxclient.game.Playground;
import de.snake.fxclient.game.ScreenText;
import de.snake.fxclient.game.ServerSounds;
import de.snake.fxclient.game.SoundSetting;
import de.snake.fxclient.game.message.InputMessage;
import de.snake.fxclient.game.message.Message;
import de.snake.fxclient.logger.MyLogger;
import de.snake.fxclient.service.DrawingService;
import de.snake.fxclient.service.SoundAndMusicService;
import javafx.application.Platform;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class CustomStompSessionHandler extends StompSessionHandlerAdapter {

    private final GameController gameController;
    private final ScreenText screenText;
    private final User user;
    private final Playground playground;
    private final ServerSounds serverSounds;
    private final DrawingService drawingService;
    private final MyLogger myLogger;
    private final SoundAndMusicService soundAndMusicService;
    private final SoundSetting soundSetting;

    public CustomStompSessionHandler(GameController gameController, ScreenText screenText, User user, Playground playground, ServerSounds serverSounds, DrawingService drawingService, MyLogger myLogger, SoundAndMusicService soundAndMusicService, SoundSetting soundSetting) {
        this.gameController = gameController;
        this.screenText = screenText;
        this.user = user;
        this.playground = playground;
        this.serverSounds = serverSounds;
        this.drawingService = drawingService;
        this.myLogger = myLogger;
        this.soundAndMusicService = soundAndMusicService;
        this.soundSetting = soundSetting;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        user.setSession(session);
        myLogger.log("New session established : " + session.getSessionId());
        session.subscribe("/user/queue/playerId", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Integer.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                myLogger.log("assigned PlayerId " + payload);

                Platform.runLater(() -> {
                    user.setPlayerId((Integer) payload);
                    //playerActiveService.sendReadyToServer();
                });
            }
        });

        session.subscribe("/topic/screenText", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ScreenText.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {

                BeanUtils.copyProperties(payload, screenText);
                if (screenText.getPlayerText() == null) {
                    screenText.setPlayerText("Bitte Spiel starten ");
                }
                if (user.isReadyToPlay() && screenText.getPlayerText().equals("Bitte Spiel starten")) {
                    user.setReadyToPlay(false);
                }
                if (!user.isReadyToPlay() && screenText.getPlayerText().equals("auf anderen Spieler warten")) {
                    screenText.setPlayerText("der andere Spieler ist bereit");
                }
                myLogger.log("Received : screenText " + screenText.getPlayerText());
                drawingService.updateScreenText();
            }
        });

        session.subscribe("/topic/serverSounds", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders stompHeaders) {
                return ServerSounds.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                myLogger.log("Received : serverSound ");
                BeanUtils.copyProperties(payload, serverSounds);
                if (!soundSetting.isSoundMuted()) {
                    soundAndMusicService.playServerSound(serverSounds.getText());
                }
            }
        });

        session.subscribe("/topic/playground", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Playground.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                BeanUtils.copyProperties(payload, playground);
                drawingService.updatePlayground();
            }
        });

        session.subscribe("/topic/messages", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return InputMessage.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                InputMessage msg = (InputMessage) payload;

                String outputFormat = System.lineSeparator() + "<" + msg.getTime() + " " + msg.getFrom() + "> " + msg.getText();
                myLogger.log("Received : Chat Message");
                Platform.runLater(() -> gameController.appendChatMessage(outputFormat));
            }
        });

        // show new player in chat
        Message joinMessage = new Message();
        joinMessage.setFrom("SYSTEM");
        joinMessage.setText("Spieler " + user.getName() + " ist hinzugekommen");
        session.send("/app/message", joinMessage);

        user.getSession().send("/app/playerId/" + user.getName(), "connect");
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        myLogger.log("An exception occurred: " + exception);
    }
}
