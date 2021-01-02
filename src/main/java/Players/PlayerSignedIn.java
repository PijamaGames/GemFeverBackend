package Players;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gemFeverBackend.GameHandler;
import com.gemFeverBackend.Room;

import Users.User;

public class PlayerSignedIn extends PlayerState {

	public PlayerSignedIn(Player player) {
		super(player);
	}

	private enum FrontendEvents{SignedOut, Error, InRoom};
	private enum BackendEvents{SignOut, Save, CreateRoom, JoinRoom};
	
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
		case CreateRoom:
			createRoom(inMsg);
			break;
		case JoinRoom:
			joinRoom(inMsg);
			break;
		}
	}
	
	public void createRoom(JsonNode inMsg) {
		if(player.inRoomState.room != null) return;
		new Room(player);
		ObjectNode outMsg = mapper.createObjectNode();
		outMsg.put("evt", FrontendEvents.InRoom.ordinal());
		outMsg.put("isHost", true);
		outMsg.put("isClient", false);
		player.setState(player.inRoomState);
		player.sendMessage(outMsg.toString());
	}
	
	public void joinRoom(JsonNode inMsg) {
		String hostName = inMsg.get("host").asText();
		Player host = PlayerManager.playersSignedIn.get(hostName);
		ObjectNode outMsg = mapper.createObjectNode();
		int error = -1;
		if(host != null) {
			if(host.getState() == host.inRoomState && host.inRoomState.room != null) {
				Room room = host.inRoomState.room;
				outMsg.put("evt", FrontendEvents.InRoom.ordinal());
				outMsg.put("isHost", false);
				outMsg.put("isClient", true);
				player.sendMessage(outMsg.toString());
				player.setState(player.inRoomState);
			} else {
				error = 0; //host is not in a room
			}
		} else {
			error = 1; //no host
		}
		if(error >= 0) {
			outMsg.put("evt", FrontendEvents.Error.ordinal());
			outMsg.put("error", error);
			player.sendMessage(outMsg.toString());
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
