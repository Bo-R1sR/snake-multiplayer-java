package de.snake.server.controller.api;

import de.snake.server.domain.CreateUserRequest;
import de.snake.server.domain.entity.User;
import de.snake.server.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // handle post request to create new user
    @PostMapping("/api/user/create")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
        return userService.createUser(createUserRequest);
    }
}
