package Players;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gemFeverBackend.GameHandler;

import Users.User;

public class PlayerSignedIn extends PlayerState {

	public PlayerSignedIn(Player player) {
		super(player);
	}

	private enum FrontendEvents{SignedOut};
	private enum BackendEvents{SignOut, Save};
	
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
		case Save:
			saveInfo(inMsg);
			break;
		}
	}
	
	public void saveInfo(JsonNode inMsg) {
		try {
			User user = GameHandler.mapper.readValue(inMsg.get("user").toString(), User.class);
			player.setUser(user);
			user.save();
			log("User data saved");
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
