package de.snake.server.service;

import de.snake.server.domain.entity.GameHistory;
import org.springframework.stereotype.Service;

import java.sql.*;

@Service
public class HistoryService {

    private final GameHistory gameHistory;
    private final Connection connection = DriverManager.getConnection("jdbc:h2:mem:snakegame", "sa", "");


    public HistoryService(GameHistory gameHistory) throws SQLException {
        this.gameHistory = gameHistory;
    }

    public ResultSet getPlayedGames(String username) throws SQLException {
        String query = "select * from game WHERE username1=" + username + " OR username2=" + username;
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        return rs;
    }
}
