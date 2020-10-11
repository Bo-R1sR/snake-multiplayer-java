package de.snake.fxclient.task;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import de.snake.fxclient.domain.User;
import javafx.concurrent.Task;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SignupTask extends Task<User> {
    private final String username;
    private final String password;

    public SignupTask(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    protected User call() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = bCryptPasswordEncoder.encode(password);

        String url = "http://localhost:8080" + "/api/user/create";
        String json = "{\"username\": \"" + username + "\", \"password\": \"" + encodedPassword + "\"}";

        try {
            HttpResponse<String> res = Unirest.post(url).header("Content-Type", "application/json").body(json).asString();
            if (res.getStatus() == 200) {
                return new User(username);
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }
}

