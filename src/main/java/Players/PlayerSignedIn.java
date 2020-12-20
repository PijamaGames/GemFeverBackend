package Players;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class PlayerSignedIn extends PlayerState {

	public PlayerSignedIn(Player player) {
		super(player);
	}

	private enum FrontendEvents{};
	private enum BackendEvents{};
	
	public void handleMessage(JsonNode inMsg) {
		/*BackendEvents event = BackendEvents.values()[(inMsg.get("event").asInt())];
		
		switch(event) {

		}*/
	}
	
	protected void begin() {
		log("state: signed in");
	}
	
	protected void finish() {
		
	}
}
