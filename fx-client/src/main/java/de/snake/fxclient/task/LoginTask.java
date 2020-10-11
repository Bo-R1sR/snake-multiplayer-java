package de.snake.fxclient.task;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import de.snake.fxclient.domain.User;
import javafx.concurrent.Task;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class LoginTask extends Task<User> {

    private final String username;

    private final String password;


    public LoginTask(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    protected User call() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = bCryptPasswordEncoder.encode(password);

        String url = "http://localhost:8080" + "/login";
        String json = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";

        try {
            HttpResponse<String> res = Unirest.post(url).header("Content-Type", "application/json").body(json).asString();
            if (res.getStatus() != 403) {
                String jsonWebToken = res.getHeaders().getFirst("Authorization");
                return new User(username, jsonWebToken);
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }
}

