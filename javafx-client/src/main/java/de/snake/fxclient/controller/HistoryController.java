package de.snake.fxclient.controller;

import com.jfoenix.controls.JFXTreeTableView;
import de.snake.fxclient.domain.GameHistory;
import de.snake.fxclient.domain.User;
import de.snake.fxclient.task.HistoryTask;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import net.rgielen.fxweaver.core.FxmlView;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@FxmlView("history-stage.fxml")
public class HistoryController {

    @Value("${server.ip}")
    private String serverIp;

    @FXML
    private TableView tableView;

    @FXML
    private TableColumn date;

    @FXML
    private TableColumn points1;

    @FXML
    private TableColumn points2;

    @FXML
    private TableColumn<String, String> decision;

    @FXML
    private TableColumn username1;

    @FXML
    private TableColumn username2;



    private final BackgroundController backgroundController;
    private final User user;

    private List<GameHistory> gameHistoryList;

    public HistoryController(BackgroundController backgroundController, User user) {
        this.backgroundController = backgroundController;
        this.user = user;
    }

    public void initialize(){
        date.setCellValueFactory(new PropertyValueFactory<GameHistory, String>("createdAt"));
        points1.setCellValueFactory(new PropertyValueFactory<GameHistory, Integer>("pointsUser1"));
        points2.setCellValueFactory(new PropertyValueFactory<GameHistory, Integer>("pointsUser2"));
        username1.setCellValueFactory(new PropertyValueFactory<GameHistory, String>("username1"));
        username2.setCellValueFactory(new PropertyValueFactory<GameHistory, String>("username2"));

        HistoryTask historyTask = new HistoryTask(user, serverIp);
        new Thread(historyTask).start();

        historyTask.setOnSucceeded((WorkerStateEvent e2) -> {
            gameHistoryList = historyTask.getResponseBody();
            ObservableList<GameHistory> oGameHistoryList = FXCollections.observableArrayList(gameHistoryList);
            ObservableList<String> decisions = FXCollections.observableArrayList();

            oGameHistoryList.forEach((GameHistory) -> {
                if(user.getName().equals(GameHistory.getUsername1())){
                    if(GameHistory.getPointsUser1() > GameHistory.getPointsUser2()){
                        decisions.add("Gewonnen");
                    }
                    else{
                        decisions.add("Verloren");
                    }
                }
                else{
                    if(GameHistory.getPointsUser1() > GameHistory.getPointsUser2()){
                        decisions.add("Verloren");
                    }
                    else{
                        decisions.add("Gewonnen");
                    }
                }

            });
            tableView.setItems(oGameHistoryList);
        });

    }
    public void back() {
        backgroundController.changeView(MainController.class);
    }

}
