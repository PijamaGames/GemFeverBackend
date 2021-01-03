package Players;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import Rooms.Room;

public class PlayerInRoom extends PlayerState {

	public Room room;
	public boolean isHost;
	public boolean isClient;
	
	
	public PlayerInRoom(Player player) {
		super(player);
	}
	
	private enum FrontendEvents{Error, Exit};
	private enum BackendEvents{Exit};
	
	public void handleMessage(JsonNode inMsg) {
		BackendEvents event = null;
		try {
			event = BackendEvents.values()[(inMsg.get("evt").asInt())];
		} catch(ArrayIndexOutOfBoundsException e) {
			
		}
		switch(event) {
		case Exit:
			exit();
			break;
		}
	}
	
	public void exit() {
		ObjectNode outMsg = mapper.createObjectNode();
		outMsg.put("evt", FrontendEvents.Exit.ordinal());
		player.setState(player.signedInState);
	}
	
	public void sendError() {
		ObjectNode outMsg = mapper.createObjectNode();
		outMsg.put("evt", FrontendEvents.Error.ordinal());
		int error = 0;
		outMsg.put("error", error);
		player.setState(player.signedInState);
	}
	
	protected void begin() {
		log("state: in room");
	}
	
	protected void finish() {
		
		
	}
	
}
