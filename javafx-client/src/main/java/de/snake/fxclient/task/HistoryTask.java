package de.snake.fxclient.task;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import de.snake.fxclient.domain.User;
import javafx.concurrent.Task;

public class HistoryTask extends Task<Integer> {

    private final User user;
    private final String serverIp;
    private String responseBody;

    public HistoryTask(User user, String serverIp) {
        this.user = user;
        this.serverIp = serverIp;
    }

    @Override
    protected Integer call() {
        String url = "http://" + serverIp + ":8080" + "/history";

        try {
            HttpResponse<String> res = Unirest.get(url).queryString("username", user.getName()).asString();
            responseBody = res.getBody();
            return res.getStatus();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getResponseBody() {
        return responseBody;
    }
}

