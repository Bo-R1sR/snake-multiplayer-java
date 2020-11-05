package de.snake.fxclient.task;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import de.snake.fxclient.domain.User;
import javafx.concurrent.Task;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class LoginTask extends Task<Integer> {

    private final User user;
    private final String serverIp;

    public LoginTask(User user, String serverIp) {
        this.user = user;
        this.serverIp = serverIp;
    }

    @Override
    protected Integer call() {
        if (user.getName().isBlank() || user.getPassword().isBlank()) {
            return -2;
        }

        String url = "http://" + serverIp + ":8080" + "/login";

        JSONObject json = new JSONObject();
        json.put("username", user.getName());
        json.put("password", user.getPassword());

        try {
            HttpResponse<String> res = Unirest.post(url).header("Content-Type", "application/json").body(json).asString();
            if (res.getStatus() == 200) {
                user.setJsonWebToken(res.getHeaders().getFirst("Authorization"));
            }
            return res.getStatus();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }
}

