package Players;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.JsonNode;
import Users.User;

public class Player {
	private final WebSocketSession session;
	public final int playerId;
	public User user = null;
	public PlayerConnected connectedState;
	public PlayerSignedIn signedInState;
	private PlayerState state = null;
	
	public Player(int playerId, WebSocketSession session) {
		this.session = session;
		this.playerId = playerId;
		this.connectedState = new PlayerConnected(this);
		this.signedInState = new PlayerSignedIn(this);
		setState(connectedState);
	}
	
	public void setState(PlayerState newState) {
		if(state != null) state.finish();
		state = newState;
		state.begin();
	}
	
	public void handleMessage(JsonNode inMsg) {
		state.handleMessage(inMsg);
	}

	public void sendMessage(String msg) throws Exception{
		this.session.sendMessage(new TextMessage(msg));
	}
}
