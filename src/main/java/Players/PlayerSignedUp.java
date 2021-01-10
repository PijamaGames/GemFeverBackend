package Players;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gemFeverBackend.GameEvent;
import com.gemFeverBackend.GameHandler;

import Items.Item;
import Users.User;

public class PlayerSignedUp extends PlayerState {

	public PlayerSignedUp(Player player) {
		super(player);
	}

	private enum FrontendEvents{SignedIn};
	private enum BackendEvents{SignIn, Save};
	
	public void handleMessage(JsonNode inMsg) {
		BackendEvents event = null;
		try {
			event = BackendEvents.values()[(inMsg.get("evt").asInt())];
		} catch(ArrayIndexOutOfBoundsException e) {
			
		}
		switch(event) {
		case SignIn:
			signIn(inMsg);
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
			Item.RemoveDuplicates(user);
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
	
	private void signIn(JsonNode inMsg) {
		
		int event = FrontendEvents.SignedIn.ordinal();
		ObjectNode outMsg = mapper.createObjectNode();
		outMsg.put("evt", event);
		GameEvent gameEvt = player.checkEvents();
		outMsg.put("hasEvent", gameEvt != null);
		outMsg.put("spanishMsg", gameEvt != null ? gameEvt.spanishMsg : "");
		outMsg.put("englishMsg", gameEvt != null ? gameEvt.englishMsg : "");
		User user = player.getUser();
		String json = "";
		try {
			json = GameHandler.mapper.writeValueAsString(user);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		outMsg.put("user", json);
		player.sendMessage(outMsg.toString());
		player.setState(player.signedInState);
	}
	
	protected void begin() {
		log("state: signed up");
		
	}
	
	protected void finish() {
		
	}
}
