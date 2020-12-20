package com.gemFeverBackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import Users.UserRepository;


@SpringBootApplication
@EnableWebSocket
@EnableMongoRepositories(basePackageClasses = UserRepository.class)
public class GemFeverBackendApplication implements WebSocketConfigurer {

	@Autowired
	private UserRepository repository;
	
	public static void main(String[] args) {
		SpringApplication.run(GemFeverBackendApplication.class, args);
		
	}
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(gameHandler(), "/player").setAllowedOrigins("*").withSockJS();
	}
	
	@Bean
	public GameHandler gameHandler() {
		return new GameHandler(repository);
	}
}
