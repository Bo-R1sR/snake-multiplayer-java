package de.snake.server;

import de.snake.server.game.Snake;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class ServerApplication {

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public Snake snake1() {
		return new Snake();
	}

	@Bean
	public Snake snake2() {
		return new Snake();
	}

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

}
