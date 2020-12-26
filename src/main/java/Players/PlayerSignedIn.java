package Players;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class PlayerSignedIn extends PlayerState {

	public PlayerSignedIn(Player player) {
		super(player);
	}

	private enum FrontendEvents{SignedOut};
	private enum BackendEvents{SignOut};
	
	public void handleMessage(JsonNode inMsg) {
		BackendEvents event = null;
		try {
			event = BackendEvents.values()[(inMsg.get("evt").asInt())];
		} catch(ArrayIndexOutOfBoundsException e) {
			
		}
		
		switch(event) {
		case SignOut:
			signOut();
			break;
		}
	}
	
	public void signOut() {
		player.setState(player.connectedState);
		player.setUser(null);
		ObjectNode outMsg = mapper.createObjectNode();
		outMsg.put("evt", FrontendEvents.SignedOut.ordinal());
		player.sendMessage(outMsg.toString());
	}
	
	protected void begin() {
		log("state: signed in");
		
	}
	
	protected void finish() {
		
	}
}
