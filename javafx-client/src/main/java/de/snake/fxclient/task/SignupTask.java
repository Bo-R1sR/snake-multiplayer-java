package de.snake.fxclient.task;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import de.snake.fxclient.domain.User;
import javafx.concurrent.Task;
import org.json.JSONObject;

public class SignupTask extends Task<Integer> {

    private final User user;

    public SignupTask(User user) {
        this.user = user;
    }

    @Override
    protected Integer call() {
        if (user.getName().isBlank() || user.getPassword().isBlank() || user.getConfirmPassword().isBlank()) {
            return -2;
        }
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            return -1;
        }

        String url = "http://localhost:8080" + "/api/user/create";

        JSONObject json = new JSONObject();
        json.put("username", user.getName());
        json.put("password", user.getPassword());

        try {
            HttpResponse<String> res = Unirest.post(url).header("Content-Type", "application/json").body(json).asString();
            return res.getStatus();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return null;
    }
}

