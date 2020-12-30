package de.snake.fxclient.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import de.snake.fxclient.domain.GameHistory;
import de.snake.fxclient.domain.User;
import javafx.concurrent.Task;

import java.util.List;

public class HistoryTask extends Task<Integer> {

    private final User user;
    private final String serverIp;
    private List<GameHistory> gameHistoryList;

    public HistoryTask(User user, String serverIp) {
        this.user = user;
        this.serverIp = serverIp;
    }

    @Override
    protected Integer call() {
        String url = "http://" + serverIp + ":8080" + "/history";

        try {
            HttpResponse<String> res = Unirest.get(url).header("Authorization", user.getJsonWebToken()).queryString("username", user.getName()).asString();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                gameHistoryList = objectMapper.readValue(res.getBody(), new TypeReference<>() {
                });
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return res.getStatus();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<GameHistory> getResponseBody() {
        return gameHistoryList;
    }
}

