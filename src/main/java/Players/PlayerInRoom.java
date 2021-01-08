package Players;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import Rooms.Room;
import Users.User;

public class PlayerInRoom extends PlayerState {

	public Room room;
	public boolean isHost;
	public boolean isClient;
	
	public PlayerInRoom(Player player) {
		super(player);
	}
	
	private enum FrontendEvents{Error, GetInfo, Exit, AddPlayer, RemovePlayer, Spawn};
	private enum BackendEvents{Exit, SendObjects, Spawn};
	
	public void handleMessage(JsonNode inMsg) {
		BackendEvents event = null;
		try {
			event = BackendEvents.values()[(inMsg.get("evt").asInt())];
		} catch(ArrayIndexOutOfBoundsException e) {
			
		}
		ObjectNode outMsg = mapper.createObjectNode();
		switch(event) {
		case Exit:
			if(isHost) {
				room.removeHost();
			} else {
				room.removeClient(player, false, false);
			}
			break;
		case SendObjects:
			/*outMsg.put("evt", FrontendEvents.GetInfo.ordinal());
			outMsg.put("objs", inMsg.get("objs").toString());*/
			room.propagateInfo(inMsg.toString(), player);
			break;
		case Spawn:
			log("spawn");
			outMsg.put("evt", FrontendEvents.Spawn.ordinal());
			outMsg.put("id", player.getUser().getId());
			room.spawnPlayer(player, outMsg);
			break;
		}
		
	}
	
	public void addPlayer(Player otherPlayer) {
		ObjectNode outMsg = mapper.createObjectNode();
		outMsg.put("evt", FrontendEvents.AddPlayer.ordinal());
		outMsg.put("roomEvt", true);
		outMsg.put("isHost", otherPlayer.inRoomState.isHost);
		outMsg.put("isClient", otherPlayer.inRoomState.isClient);
		outMsg.put("id", otherPlayer.getUser().getId());
		
		User user = otherPlayer.getUser();
		outMsg.put("avatar_bodyType", user.getAvatar_bodyType());
		outMsg.put("avatar_skinTone", user.getAvatar_skinTone());
		outMsg.put("avatar_color", user.getAvatar_color());
		outMsg.put("avatar_face", user.getAvatar_face());
		outMsg.put("avatar_hat", user.getAvatar_hat());
		outMsg.put("avatar_frame", user.getAvatar_frame());
		
		player.sendMessage(outMsg.toString());
	}
	
	public void removePlayer(Player player) {
		ObjectNode outMsg = mapper.createObjectNode();
		outMsg.put("evt", FrontendEvents.RemovePlayer.ordinal());
		outMsg.put("player", player.getUser().getId());
		player.sendMessage(outMsg.toString());
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
