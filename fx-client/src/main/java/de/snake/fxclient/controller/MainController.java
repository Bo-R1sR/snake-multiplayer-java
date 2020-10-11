package de.snake.fxclient.controller;

import de.snake.fxclient.FxClientApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Objects;

@Controller
public class MainController {

    // singleton
    private static MainController instance;



    private String jsonWebToken;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String username;

    @FXML
    StackPane viewHolder;
    @FXML
    Label title;

    // singleton access
    public static MainController getInstance() {
        return instance;
    }

    public String getJsonWebToken() {
        return jsonWebToken;
    }

    public void setJsonWebToken(String jsonWebToken) {
        this.jsonWebToken = jsonWebToken;
    }

    @FXML
    public void initialize() {
        instance = this;
    }

    public void changeView(String fxmlFilename) {
        Node view = loadFXML("view/" + fxmlFilename + ".fxml");
        viewHolder.getChildren().setAll(view); // clears the list of child elements and adds the view as a new child element
    }

    public Node loadFXML(String fxmlFilename) {
        try {
            return FXMLLoader.load(Objects.requireNonNull(FxClientApplication.class.getClassLoader().getResource(fxmlFilename)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
