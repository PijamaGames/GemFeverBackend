package Players;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class PlayerConnected extends PlayerState {

	public PlayerConnected(Player player) {
		super(player);
	}

	public enum FrontendEvents{SignedIn};
	public enum BackendEvents{SignIn, SignUp};
	
	public void handleMessage(JsonNode inMsg) {
		BackendEvents event = BackendEvents.values()[(inMsg.get("event").asInt())];
		switch(event) {
		case SignUp:
			signIn(inMsg);
			break;
		case SignIn:
			signUp(inMsg);
			break;
		}
	}
	
	private void signUp(JsonNode inMsg) {
		
		
		
		player.setState(player.signedInState);
	}
	
	private void signIn(JsonNode inMsg) {
		
		
		player.setState(player.signedInState);
	}
	
	protected void begin() {
		
	}
	
	protected void finish() {
		int event = FrontendEvents.SignedIn.ordinal();
		ObjectNode outMsg = mapper.createObjectNode();
		outMsg.put("event", event);
	}
}
