package de.snake.fxclient.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXToggleButton;
import de.snake.fxclient.domain.User;
import de.snake.fxclient.game.*;
import de.snake.fxclient.game.composite.*;
import de.snake.fxclient.game.message.Message;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;
import java.util.List;

@Component
@FxmlView("game-stage.fxml")
public class GameController {

    private final BackgroundController backgroundController;
    private final User user;
    private final Playground playground;
    private final ScreenText screenText;
    private final Level level;

    private SnakeDirectionEnum direction;
    private GraphicsContext gc;
    private Color colorSnake1 = Color.BLUE;
    private Color colorSnake1Border = Color.LIGHTBLUE;
    private Color colorSnake2 = Color.GREEN;
    private Color colorSnake2Border = Color.LIGHTGREEN;
    private Boolean isSoundMuted = Boolean.TRUE;
    // Musik
    private final String musicPath = "src/main/resources/sounds/test.mp3";
    Media musicMedia = new Media(new File(musicPath).toURI().toString());
    MediaPlayer musicPlayer = new MediaPlayer(musicMedia);

    // Essen
    private final String eatPath = "src/main/resources/sounds/test.mp3";
    Media eatMedia = new Media(new File(eatPath).toURI().toString());
    MediaPlayer eatPlayer = new MediaPlayer(eatMedia);

    // Runde vorbei
    private final String roundOverPath = "src/main/resources/sounds/test.mp3";
    Media roundOverMedia = new Media(new File(roundOverPath).toURI().toString());
    MediaPlayer roundOverPlayer = new MediaPlayer(roundOverMedia);

    // Spiel vorbei
    private final String gameOverPath = "src/main/resources/sounds/test.mp3";
    Media gameOverMedia = new Media(new File(gameOverPath).toURI().toString());
    MediaPlayer gameOverPlayer = new MediaPlayer(gameOverMedia);

    // Countdown
    private final String countdownPath = "src/main/resources/sounds/test.mp3";
    Media countdownMedia = new Media(new File(countdownPath).toURI().toString());
    MediaPlayer countdownPlayer = new MediaPlayer(countdownMedia);

    // Game Start
    private final String gameStartPath = "src/main/resources/sounds/test.mp3";
    Media gameStartMedia = new Media(new File(gameStartPath).toURI().toString());
    MediaPlayer gameStartPlayer = new MediaPlayer(gameStartMedia);

    @FXML
    private TextArea chatArea;
    @FXML
    private TextField chatMess;
    @FXML
    private Canvas gameCanvas;
    @FXML
    private JFXToggleButton musicSwitch;
    @FXML
    private JFXToggleButton soundSwitch;
    @FXML
    private JFXSlider volumeSlider;
    @FXML
    private JFXComboBox<String> colorPicker;

    public GameController(BackgroundController backgroundController, User user, Playground playground, ScreenText screenText, Level level) {
        this.backgroundController = backgroundController;
        this.user = user;
        this.playground = playground;
        this.screenText = screenText;
        this.level = level;
    }

    @FXML
    public void initialize() {
        gc = gameCanvas.getGraphicsContext2D();
        initializeColorList();
        Shape background = new Square(gc, Color.BLACK, new Point2D(0, 0), playground.getWidth() * playground.getSnakeBodySize(), playground.getHeight() * playground.getSnakeBodySize());
        background.draw();
    }

    // button for back to main menu - disconnect ws session
    public void back() {
        if (user.getSession() != null) {
            user.getSession().disconnect();
        }
        backgroundController.changeView(MainController.class);
    }

    public void initializeGame() {
        // send username to server
        user.getSession().send("/app/playerId/" + user.getName(), "connect");

        // assign arrow keys for game control
        Scene scene = backgroundController.getViewHolder().getParent().getScene();
        scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            // only send key if it is arrow
            if (key.getCode() == KeyCode.UP) {
                direction = SnakeDirectionEnum.UP;
                key.consume();
                user.getSession().send("/app/direction" + user.getPlayerId(), direction);
            }
            if (key.getCode() == KeyCode.LEFT) {
                direction = SnakeDirectionEnum.LEFT;
                key.consume();
                user.getSession().send("/app/direction" + user.getPlayerId(), direction);
            }
            if (key.getCode() == KeyCode.DOWN) {
                direction = SnakeDirectionEnum.DOWN;
                key.consume();
                user.getSession().send("/app/direction" + user.getPlayerId(), direction);
            }
            if (key.getCode() == KeyCode.RIGHT) {
                direction = SnakeDirectionEnum.RIGHT;
                key.consume();
                user.getSession().send("/app/direction" + user.getPlayerId(), direction);
            }
        });
    }

    // called from SessionHandler when ID for client is fixed
    public void startGame() {
        // set readiness in client to make sure correct ready message is displayed for player 1 or 2
        user.setReadyToPlay(true);
        // send readiness to sever
        user.getSession().send("/app/playerActive/" + user.getPlayerId(), "connect");
    }

    // called from SessionHandler when Countdown ScreenText arrives
    public void updateScreenText() {
        Shape background = new Square(gc, Color.BLACK, new Point2D(0, 0), playground.getWidth() * playground.getSnakeBodySize(), playground.getHeight() * playground.getSnakeBodySize());
        Shape screenT = new Text(gc, new Point2D(100, 250), screenText.getPlayerText(), Color.WHITE, true);
        Shape counterText = new CompositeShape(gc, List.of(background, screenT));
        counterText.draw();
    }

    public void playServerSound(String serverSound){
        if(!isSoundMuted){
            switch (serverSound) {
                case "Food" -> {
                    eatPlayer.stop();
                    eatPlayer.play();
                }
                case "Countdown" -> {
                    countdownPlayer.stop();
                    countdownPlayer.play();
                }
                case "GameStart" -> {
                    gameStartPlayer.stop();
                    gameStartPlayer.play();
                }
            }
        }
    }

    // called from SessionHandler when new Playground arrives
    public void updatePlayground() {
        // special screen only for game over
        if (playground.isGameOver()) {
            Shape gameOverText = new Text(gc, new Point2D(100, 250), "GAME OVER", Color.WHITE, true);
            Shape score1 = new Text(gc, new Point2D(200, 100), "" + playground.getSnake2().getPoints(), colorSnake1, false);
            Shape space = new Text(gc, new Point2D(250, 100), " : ", Color.WHITE, false);
            Shape score2 = new Text(gc, new Point2D(300, 100), "" + playground.getSnake1().getPoints(), colorSnake2, false);
            Shape gameOverScreen = new CompositeShape(gc, List.of(gameOverText, score1, space, score2));
            gameOverScreen.draw();
            user.setReadyToPlay(false);
            return;
        }
        // regular game screen
        Shape background = new Square(gc, Color.BLACK, new Point2D(0, 0), playground.getWidth() * playground.getSnakeBodySize(), playground.getHeight() * playground.getSnakeBodySize());
        Shape level = new CompositeShape(gc, createLevel(playground.getLevelNumber(), Color.WHITE, Color.LIGHTGREY));
        Shape snake1 = new CompositeShape(gc, createSnake(playground.getSnake1(), colorSnake1Border, colorSnake1));
        Shape snake2 = new CompositeShape(gc, createSnake(playground.getSnake2(), colorSnake2Border, colorSnake2));
        Shape snakes = new CompositeShape(gc, List.of(snake1, snake2));
        Shape food = new Circle(gc, playground.getFood().getFoodColor(), new Point2D(playground.getFood().getFoodPositionX() * playground.getSnakeBodySize(), playground.getFood().getFoodPositionY() * playground.getSnakeBodySize()), playground.getSnakeBodySize());
        Shape playground = new CompositeShape(gc, List.of(background, level, snakes, food));
        playground.draw();
    }

    public List<Shape> createSnake(Snake snake, Color colorBack, Color colorFront) {
        List<Shape> snakeList = new ArrayList<>();
        for (SnakeBodyPart sbp : snake.getSnakeBody()) {
            Shape squareBack = new Square(gc, colorBack,
                    new Point2D(sbp.getPositionX() * playground.getSnakeBodySize(), sbp.getPositionY() * playground.getSnakeBodySize()),
                    playground.getSnakeBodySize() - 1, playground.getSnakeBodySize() - 1);
            snakeList.add(squareBack);

            Shape squareFront = new Square(gc, colorFront,
                    new Point2D(sbp.getPositionX() * playground.getSnakeBodySize(), sbp.getPositionY() * playground.getSnakeBodySize()),
                    playground.getSnakeBodySize() - 2, playground.getSnakeBodySize() - 2);
            snakeList.add(squareFront);

            // position where snake can be bitten
            if (sbp.getColor() == 3) {
                Shape biteCircle = new Circle(gc, sbp.getColor(),
                        new Point2D(sbp.getPositionX() * playground.getSnakeBodySize(), sbp.getPositionY() * playground.getSnakeBodySize()), playground.getSnakeBodySize() - 2);
                snakeList.add(biteCircle);
            }

            if (snake.isImmortal()) {
                Shape immortalCircle = new Circle(gc, 2,
                        new Point2D(sbp.getPositionX() * playground.getSnakeBodySize(), sbp.getPositionY() * playground.getSnakeBodySize()), playground.getSnakeBodySize() - 2);
                snakeList.add(immortalCircle);
            }
        }
        return snakeList;
    }

    public List<Shape> createLevel(int levelNumber, Color colorBack, Color colorFront) {
        List<Shape> levelList = new ArrayList<>();
        for (SnakeBodyPart levelPart : level.getAllLevels().get(levelNumber)) {
            Shape squareBack = new Square(gc, colorBack,
                    new Point2D(levelPart.getPositionX() * playground.getSnakeBodySize(), levelPart.getPositionY() * playground.getSnakeBodySize()),
                    playground.getSnakeBodySize() - 1, playground.getSnakeBodySize() - 1);
            levelList.add(squareBack);

            Shape squareFront = new Square(gc, colorFront,
                    new Point2D(levelPart.getPositionX() * playground.getSnakeBodySize(), levelPart.getPositionY() * playground.getSnakeBodySize()),
                    playground.getSnakeBodySize() - 2, playground.getSnakeBodySize() - 2);
            levelList.add(squareFront);
        }
        return levelList;
    }

    private Message getNewMessage() {
        Message msg = new Message();
        msg.setFrom(user.getName());
        msg.setText(chatMess.getText());
        return msg;
    }

    public void appendChatMessage(String message) {
        chatArea.appendText(message);
    }

    public void submitMessage() {
        user.getSession().send("/app/message", getNewMessage());
        chatMess.setText("");
    }

    public void toggleMusic(){
        if (musicSwitch.isSelected()){
            musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            musicPlayer.play();
            System.out.println("Musik an");
        }
        else{
            musicPlayer.pause();
            System.out.println("Musik an");
        }
    }

    public void toggleSounds(){
        if (soundSwitch.isSelected()){
            System.out.println("Sounds an");
            isSoundMuted = Boolean.FALSE;
        }
        else{
            System.out.println("Sounds aus");
            isSoundMuted = Boolean.TRUE;
        }
    }

    public void changeVolume(){
        musicPlayer.setVolume(volumeSlider.getValue());
        System.out.println("Volume: " + volumeSlider.getValue());
    }

    private void initializeColorList(){
        colorPicker.getItems().clear();
        colorPicker.getItems().addAll("Blau/Grün", "Gelb/Rosa", "Lila/Cyan");
        colorPicker.getSelectionModel().select("Blau/Grün");
    }

    public void changeColor(){
        String choosenColor = colorPicker.getValue();
        switch (choosenColor) {
            case "Blau/Grün" -> {
                colorSnake1 = Color.BLUE;
                colorSnake1Border = Color.LIGHTBLUE;
                colorSnake2 = Color.GREEN;
                colorSnake2Border = Color.LIGHTGREEN;
            }
            case "Gelb/Rosa" -> {
                colorSnake1 = Color.YELLOW;
                colorSnake1Border = Color.LIGHTYELLOW;
                colorSnake2 = Color.PINK;
                colorSnake2Border = Color.LIGHTPINK;
            }
            case "Lila/Cyan" -> {
                colorSnake1 = Color.VIOLET;
                colorSnake1Border = Color.PALEVIOLETRED;
                colorSnake2 = Color.CYAN;
                colorSnake2Border = Color.LIGHTCYAN;
            }
        }

    }
}
