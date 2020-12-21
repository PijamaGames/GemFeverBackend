package Players;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class PlayerSignedUp extends PlayerState {

	public PlayerSignedUp(Player player) {
		super(player);
	}

	private enum FrontendEvents{SignedIn};
	private enum BackendEvents{SignIn};
	
	public void handleMessage(JsonNode inMsg) {
		BackendEvents event = BackendEvents.values()[(inMsg.get("evt").asInt())];
		switch(event) {
		case SignIn:
			signIn(inMsg);
			break;
		}
	}
	
	private void signIn(JsonNode inMsg) {
	
		int event = FrontendEvents.SignedIn.ordinal();
		ObjectNode outMsg = mapper.createObjectNode();
		outMsg.put("evt", event);
		player.sendMessage(outMsg.toString());
		player.setState(player.signedInState);
	}
	
	protected void begin() {
		log("state: signed up");
		
	}
	
	protected void finish() {
		
	}
}
