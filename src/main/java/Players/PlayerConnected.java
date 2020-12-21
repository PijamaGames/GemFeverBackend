package Players;

import java.util.Optional;

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
		BackendEvents event = BackendEvents.values()[(inMsg.get("evt").asInt())];
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
		
		JsonNode userNode = inMsg.get("user");
		if(userNode != null) {
			User user = new User(
					userNode.get("id").asText(),
					userNode.get("password").asText(),
					userNode.get("avatar_bodyType").asInt(),
					userNode.get("avatar_skinTone").asInt(),
					userNode.get("avatar_color").asInt(),
					userNode.get("avatar_face").asInt(),
					userNode.get("avatar_hat").asInt(),
					userNode.get("avatar_frame").asInt()
					);
			player.user = user;
			player.user.save();
			
			int event = FrontendEvents.SignedUp.ordinal();
			ObjectNode outMsg = mapper.createObjectNode();
			outMsg.put("evt", event);
			player.sendMessage(outMsg.toString());
			
			player.setState(player.signedUpState);
		}
	}
	
	private void signIn(JsonNode inMsg) {
		
		String username = inMsg.get("username").asText();
		String password = inMsg.get("password").asText();
		
		//TODO: Comprobar si los datos de usuarios son correctos
		//Si no lo son, enviar wrong data
		//ELSE:
		ObjectNode outMsg = mapper.createObjectNode();
		int event;
		boolean wrongData = false;
		int errorCode = -1;
		Optional<User> userOp = GameHandler.repo.findById(username);
		if(userOp.isPresent()) {
			User user = userOp.get();
			if(!user.getPassword().equals(password)) {
				wrongData = true;
				errorCode = 0;
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
