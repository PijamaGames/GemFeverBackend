package Players;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import Rooms.Room;
import Users.User;

public class PlayerInRoom extends PlayerState {

	public Room room;
	public boolean isHost;
	public boolean isClient;
	public boolean spawned;
	
	public PlayerInRoom(Player player) {
		super(player);
	}
	
	private enum FrontendEvents{Error, GetInfo, Exit, AddPlayer, RemovePlayer, Spawn, ChangeScene};
	private enum BackendEvents{Exit, SendObjects, Spawn, ChangeScene, SaveGems};
	
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
				room.removeHost(false);
			} else {
				room.removeClient(player, false, false);
			}
			break;
		case SendObjects:
			room.propagateInfo(inMsg.toString(), player);
			break;
		case Spawn:
			log("spawn " + player.getUser().getId());
			spawned = true;
			outMsg.put("evt", FrontendEvents.Spawn.ordinal());
			outMsg.put("id", player.getUser().getId());
			room.spawnPlayer(player, outMsg);
			break;
		case ChangeScene:
			String scene = inMsg.get("id").asText();
			log("changeScene: " + scene);
			outMsg.put("evt", FrontendEvents.ChangeScene.ordinal());
			outMsg.put("id", scene);
			room.changeScene(outMsg, inMsg.get("playing").asBoolean());
			break;
		case SaveGems:
			User user = player.getUser();
			user.setGems(user.getGems()+inMsg.get("gems").asInt());
			user.save();
		}
	}
	
	public void addPlayer(Player otherPlayer) {
		ObjectNode outMsg = mapper.createObjectNode();
		outMsg.put("evt", FrontendEvents.AddPlayer.ordinal());
		outMsg.put("roomEvt", true);
		outMsg.put("isHost", otherPlayer.inRoomState.isHost);
		outMsg.put("isClient", otherPlayer.inRoomState.isClient);
		outMsg.put("id", otherPlayer.getUser().getId());
		outMsg.put("spawned", otherPlayer.inRoomState.spawned);
		
		User user = otherPlayer.getUser();
		outMsg.put("avatar_bodyType", user.getAvatar_bodyType());
		outMsg.put("avatar_skinTone", user.getAvatar_skinTone());
		outMsg.put("avatar_color", user.getAvatar_color());
		outMsg.put("avatar_face", user.getAvatar_face());
		outMsg.put("avatar_hat", user.getAvatar_hat());
		outMsg.put("avatar_frame", user.getAvatar_frame());
		
		player.sendMessage(outMsg.toString());
	}
	
	public void removePlayer(Player otherPlayer) {
		ObjectNode outMsg = mapper.createObjectNode();
		outMsg.put("evt", FrontendEvents.RemovePlayer.ordinal());
		outMsg.put("id", otherPlayer.getUser().getId());
		player.sendMessage(outMsg.toString());
	}
	
	public void exit() {
		ObjectNode outMsg = mapper.createObjectNode();
		outMsg.put("evt", FrontendEvents.Exit.ordinal());
		player.setState(player.signedInState);
		player.sendMessage(outMsg.toString());
	}
	
	public void sendError() {
		ObjectNode outMsg = mapper.createObjectNode();
		outMsg.put("evt", FrontendEvents.Error.ordinal());
		int error = 0;
		outMsg.put("error", error);
		player.setState(player.signedInState);
		player.sendMessage(outMsg.toString());
	}
	
	protected void begin() {
		log("state: in room");
		spawned = false;
	}
	
	protected void finish() {
		spawned = false;
		room = null;
		isClient = false;
		isHost = false;
	}
	
}
