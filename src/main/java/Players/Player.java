package Players;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.JsonNode;
import com.gemFeverBackend.GameEvent;
import com.gemFeverBackend.GameEventsManager;
import com.gemFeverBackend.GameHandler;

import Users.User;

public class Player {
	private final WebSocketSession session;
	public final int playerId;
	public PlayerConnected connectedState;
	public PlayerSignedIn signedInState;
	public PlayerSignedUp signedUpState;
	private PlayerState state = null;
	private User user = null;
	
	private final boolean DEBUG = true;
	private void log(String msg) {
		if(DEBUG) {
			System.out.println("[PLAYER " + playerId + "] " + msg);
		}
	}
	
	public Player(int playerId, WebSocketSession session) {
		this.session = session;
		this.playerId = playerId;
		this.connectedState = new PlayerConnected(this);
		this.signedInState = new PlayerSignedIn(this);
		this.signedUpState = new PlayerSignedUp(this);
		setState(connectedState);
	}
	
	public void connect() {
		PlayerManager.Add(this);
	}
	
	public void disconnect() {
		PlayerManager.Remove(this);
		if(user != null) {
			PlayerManager.usersInUse.remove(user.getId());
		}
	}
	
	public void setState(PlayerState newState) {
		if(state != null) state.finish();
		state = newState;
		state.begin();
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User _user) {
		if(user != null) {
			PlayerManager.usersInUse.remove(user.getId());
		}
		user = _user;
		if(user != null) {
			PlayerManager.usersInUse.put(user.getId(), user);
		}
	}
	
	public void handleMessage(JsonNode inMsg) {
		if(inMsg.get("evt").asInt() < 0) return;
		state.handleMessage(inMsg);
	}
	
	public GameEvent checkEvents() {
		if(user == null) return null;
		log("CHECKING EVENTS");
		LocalDate now = LocalDate.now();
		Set<String> userEvents = new HashSet<String>(Arrays.asList(user.getEventsAttended()));
		for(GameEvent evt : GameEventsManager.events) {
			if(!now.isBefore(evt.start) && !now.isAfter(evt.end)) {
				if(!userEvents.contains(evt.name)) {
					log("ADDING EVENT: " + evt.name);
					evt.AddToUser(user);
					user.save();
					return evt;
				}
			}
		}
		return null;
	}

	public void sendMessage(String msg){
		try {
			this.session.sendMessage(new TextMessage(msg));
		} catch(Exception e){
			log("Error sending message");
		}
	}
}
