package Players;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import Users.User;

public class PlayerConnected extends PlayerState {

	public PlayerConnected(Player player) {
		super(player);
	}

	private enum FrontendEvents{SignedIn};
	private enum BackendEvents{SignIn, SignUp};
	
	public void handleMessage(JsonNode inMsg) {
		BackendEvents event = BackendEvents.values()[(inMsg.get("event").asInt())];
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
			player.setState(player.signedInState);
		}
	}
	
	private void signIn(JsonNode inMsg) {
		
		
		player.setState(player.signedInState);
	}
	
	protected void begin() {
		log("state: connected");
	}
	
	protected void finish() {
		int event = FrontendEvents.SignedIn.ordinal();
		ObjectNode outMsg = mapper.createObjectNode();
		outMsg.put("event", event);
	}
}
