package de.snake.fxclient;

import de.snake.fxclient.controller.BackgroundController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;


public class JavaFxApplication extends Application {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        String[] args = getParameters().getRaw().toArray(new String[0]);

        this.applicationContext = new SpringApplicationBuilder()
                .sources(ClientApplication.class)
                .run(args);
    }

    @Override
    public void start(Stage stage) {
        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(BackgroundController.class);
        Scene scene = new Scene(root, 1102, 662);
        stage.setTitle("Snake 42");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.getIcons().add(new Image("file:src/main/resources/icon/snake.png"));
        scene.getStylesheets().add("file:src/main/resources/css/snakeStyleSheet.css");
        stage.show();
    }

    @Override
    public void stop() {
        this.applicationContext.close();
        Platform.exit();
    }

}
