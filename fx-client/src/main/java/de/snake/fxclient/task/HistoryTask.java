package de.snake.fxclient.task;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import de.snake.fxclient.domain.User;
import javafx.concurrent.Task;


public class HistoryTask extends Task<User> {

    private final String token;

    private final String username;




    public HistoryTask(String token, String username) {
        this.token = token;
        this.username = username;
    }

    @Override
    protected User call() {


        String url = "http://localhost:8080" + "/history";
        String json = "{\"username\": \"" + username + "\"}";


        try {
            HttpResponse<String> res =  Unirest.get(url).header("Authorization", token).queryString("username", username).asString();
            if (res.getStatus() == 200) {
                System.out.println(res.getBody());
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }



        //HttpResponse<String> res = Unirest.post(url).header("Content-Type", "application/json").body(json).asString();
//        try {
//            HttpResponse<String> res = Unirest.post(url).header("Authorization", token).body(json).asString();
//        } catch (UnirestException e) {
//            e.printStackTrace();
//        }
        //Unirest.get(url).header("Authorization", token);


//        try {
//            HttpResponse<String> res = Unirest.post(url).header("Content-Type", "application/json").body(json).asString();
//            if (res.getStatus() != 403) {
//                String jsonWebToken = res.getHeaders().getFirst("Authorization");
//                return new User(username, jsonWebToken);
//            }
//        } catch (UnirestException e) {
//            e.printStackTrace();
//        }
        return null;
    }
}

