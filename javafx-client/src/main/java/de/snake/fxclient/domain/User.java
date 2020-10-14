package de.snake.fxclient.domain;

import org.springframework.stereotype.Component;

@Component
public class User {

    private Integer id;
    private String name;
    private String password;
    private String confirmPassword;
    private String jsonWebToken;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getJsonWebToken() {
        return jsonWebToken;
    }

    public void setJsonWebToken(String jsonWebToken) {
        this.jsonWebToken = jsonWebToken;
    }

    public void reset() {
        this.setId(null);
        this.setName(null);
        this.setPassword(null);
        this.setConfirmPassword(null);
        this.setJsonWebToken(null);
    }
}

