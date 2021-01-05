package de.snake.fxclient.controller;

import de.snake.fxclient.domain.GameHistory;
import de.snake.fxclient.domain.GameHistoryTable;
import de.snake.fxclient.domain.User;
import de.snake.fxclient.task.HistoryTask;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@FxmlView("history-stage.fxml")
public class HistoryController {

    private final BackgroundController backgroundController;
    private final User user;
    @Value("${server.ip}")
    private String serverIp;
    @FXML
    private TableView<GameHistoryTable> tableView;
    @FXML
    private TableColumn<GameHistoryTable, String> date;
    @FXML
    private TableColumn<GameHistoryTable, Integer> yourPoints;
    @FXML
    private TableColumn<GameHistoryTable, Integer> enemyPoints;
    @FXML
    private TableColumn<GameHistoryTable, String> decision;
    @FXML
    private TableColumn<GameHistoryTable, String> enemyName;
    private List<GameHistory> gameHistoryList;

    public HistoryController(BackgroundController backgroundController, User user) {
        this.backgroundController = backgroundController;
        this.user = user;
    }

    public void initialize() {
        // Configure the population of the columns
        date.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        yourPoints.setCellValueFactory(new PropertyValueFactory<>("yourPoints"));
        enemyPoints.setCellValueFactory(new PropertyValueFactory<>("enemyPoints"));
        enemyName.setCellValueFactory(new PropertyValueFactory<>("enemyName"));
        decision.setCellValueFactory(new PropertyValueFactory<>("decision"));

        // Start the history task
        HistoryTask historyTask = new HistoryTask(user, serverIp);
        new Thread(historyTask).start();

        // If the history task succeeds
        historyTask.setOnSucceeded((WorkerStateEvent e2) -> {
            gameHistoryList = historyTask.getResponseBody();
            // New observable list
            ObservableList<GameHistoryTable> gameHistoryTableList = FXCollections.observableArrayList();
            // For each object in the response
            gameHistoryList.forEach((GameHistory) -> {
                GameHistoryTable gameHistoryTable = new GameHistoryTable();
                // Make the string more readable
                gameHistoryTable.setCreatedAt(GameHistory.getCreatedAt().substring(0, 19).replace("T", " "));
                // User is player 1
                if (user.getName().equals(GameHistory.getUsername1())) {
                    gameHistoryTable.setYourPoints(GameHistory.getPointsUser1());
                    gameHistoryTable.setEnemyPoints(GameHistory.getPointsUser2());
                    gameHistoryTable.setEnemyName(GameHistory.getUsername2());
                    // Check who won
                    if (GameHistory.getPointsUser1() > GameHistory.getPointsUser2()) {
                        gameHistoryTable.setDecision("Gewonnen");
                    } else if (GameHistory.getPointsUser1() == GameHistory.getPointsUser2()) {
                        gameHistoryTable.setDecision("Unentschieden");
                    } else {
                        gameHistoryTable.setDecision("Verloren");
                    }
                }
                // User is player 2
                else {
                    gameHistoryTable.setYourPoints(GameHistory.getPointsUser2());
                    gameHistoryTable.setEnemyPoints(GameHistory.getPointsUser1());
                    gameHistoryTable.setEnemyName(GameHistory.getUsername1());
                    // Check who won
                    if (GameHistory.getPointsUser1() > GameHistory.getPointsUser2()) {
                        gameHistoryTable.setDecision("Verloren");
                    } else if (GameHistory.getPointsUser1() == GameHistory.getPointsUser2()) {
                        gameHistoryTable.setDecision("Unentschieden");
                    } else {
                        gameHistoryTable.setDecision("Gewonnen");
                    }
                }
                // Add to the list
                gameHistoryTableList.add(gameHistoryTable);
            });
            // Display data in the table
            tableView.setItems(gameHistoryTableList);
        });

    }

    public void back() {
        backgroundController.changeView(MainController.class);
    }

}
