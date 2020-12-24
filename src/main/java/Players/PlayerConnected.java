package Players;

import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gemFeverBackend.GameHandler;

import Users.User;

public class PlayerConnected extends PlayerState {

	public PlayerConnected(Player player) {
		super(player);
	}

	private enum FrontendEvents{SignedIn, SignedUp, WrongData};
	private enum BackendEvents{SignIn, SignUp};
	
	public void handleMessage(JsonNode inMsg) {
		BackendEvents event = null;
		try {
			event = BackendEvents.values()[(inMsg.get("evt").asInt())];
		} catch(ArrayIndexOutOfBoundsException e) {
			
		}
		
		switch(event) {
		case SignUp:
			signUp(inMsg);
			break;
		case SignIn:
			signIn(inMsg);
			break;
		}
	}
	
	private void signUp(JsonNode inMsg) {
		String username = inMsg.get("username").asText();
		if(username != null) {
			String password = inMsg.get("password").asText();
			Optional<User> userOp = GameHandler.repo.findById(username);
			int errorCode = -1;
			int event;
			User user = null;
			if(userOp.isPresent()) {
				errorCode = 3;
				event = FrontendEvents.WrongData.ordinal();
			} else {
				user = new User(username, password);
				player.setUser(user);
				user.save();
				event = FrontendEvents.SignedUp.ordinal();
				player.setState(player.signedUpState);
			}
			ObjectNode outMsg = mapper.createObjectNode();
			outMsg.put("evt", event);
			outMsg.put("error", errorCode);
			if(user != null) {
				String json = "";
				try {
					json = GameHandler.mapper.writeValueAsString(user);
					log(json);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				outMsg.put("user", json);
			}
			player.sendMessage(outMsg.toString());
		}
	}
	
	private void signIn(JsonNode inMsg) {
		
		String username = inMsg.get("username").asText();
		String password = inMsg.get("password").asText();
		
		ObjectNode outMsg = mapper.createObjectNode();
		int event;
		boolean wrongData = false;
		/*
		 * 0: Wrong password
		 * 1: User does not exist
		 * 2: User already in use
		 */
		int errorCode = -1;
		Optional<User> userOp = GameHandler.repo.findById(username);
		User user = null;
		if(userOp.isPresent()) {
			user = userOp.get();
			if(!user.getPassword().equals(password)) {
				wrongData = true;
				errorCode = 0;
			}
			if(PlayerManager.usersInUse.containsKey(username)) {
				wrongData = true;
				errorCode = 2;
			}
		} else {
			wrongData = true;
			errorCode = 1;
		}
		if(wrongData) {
			event = FrontendEvents.WrongData.ordinal();
			log("wrong data");
		} else {
			event = FrontendEvents.SignedIn.ordinal();
			player.setState(player.signedInState);
			user.save();
			player.setUser(user);
			String json = "";
			try {
				json = GameHandler.mapper.writeValueAsString(user);
				log(json);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			outMsg.put("user", json);
		}
		outMsg.put("evt", event);
		outMsg.put("error", errorCode);
		player.sendMessage(outMsg.toString());
		
	}
	
	protected void begin() {
		log("state: connected");
	}
	
	protected void finish() {
		
		
	}
}
