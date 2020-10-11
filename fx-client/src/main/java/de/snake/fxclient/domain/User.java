package de.snake.fxclient.domain;

public class User {

    public Integer id;
    public String name;
    public String jsonWebToken;

    public User(String name, String jsonWebToken) {
        this.name = name;
        this.jsonWebToken = jsonWebToken;
    }
    public User(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJsonWebToken() {
        return jsonWebToken;
    }

    public void setJsonWebToken(String jsonWebToken) {
        this.jsonWebToken = jsonWebToken;
    }
}

