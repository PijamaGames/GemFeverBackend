package com.gemFeverBackend;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import Players.Player;
import Players.PlayerManager;
import Users.UserRepository;

public class GameHandler extends TextWebSocketHandler {

	private AtomicInteger playerId = new AtomicInteger(0);
	private static final String PLAYER_ATTRIBUTE = "PLAYER";
	private static final boolean DEBUG_MODE = true;
	public static ObjectMapper mapper = new ObjectMapper();

	public static UserRepository repo;
	
	public GameHandler(UserRepository repository) {
		repo = repository;
	}

	private static void log(String str) {
		if (DEBUG_MODE) {
			System.out.println("[WS HANDLER] " + str);
		}
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		Player player = new Player(playerId.incrementAndGet(), session);
		player.connect();
		
		session.getAttributes().put(PLAYER_ATTRIBUTE, player);
		//repository.save(new User("Pedro", "la vida es dura", 0,1,2,3,0,1));
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		try {
			JsonNode inMsg = mapper.readTree(message.getPayload());
			Player player = (Player) session.getAttributes().get(PLAYER_ATTRIBUTE);
			player.handleMessage(inMsg);
		} catch (Exception e) {
			System.err.println("Exception processing message" + message.getPayload());
			e.printStackTrace(System.err);
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		Player player = (Player) session.getAttributes().get(PLAYER_ATTRIBUTE);
		player.disconnect();
	}
}
