package Players;

import java.util.ArrayList;
import java.util.Random;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gemFeverBackend.GameHandler;

import Rooms.PlayerRoomInfo;
import Rooms.Room;
import Users.User;

public class PlayerSignedIn extends PlayerState {

	Random random;
	
	public PlayerSignedIn(Player player) {
		super(player);
		random = new Random();
	}

	private enum FrontendEvents{SignedOut, Error, InRoom, GetRooms};
	private enum BackendEvents{SignOut, Save, CreateRoom, JoinRoom, RequestRooms};
	
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
		case RequestRooms:
			requestRooms();
			break;
		}
	}
	
	public void requestRooms() {
		ObjectNode outMsg = mapper.createObjectNode();
		outMsg.put("evt", FrontendEvents.GetRooms.ordinal());
		ArrayNode infos = outMsg.putArray("players");
		for(Player p : PlayerManager.players){
			if(p.getState() == p.inRoomState) {
				Room room = p.inRoomState.room;
				if(room != null && room.admitsClients()) {
					String json;
					try {
						json = GameHandler.mapper.writeValueAsString(new 						PlayerRoomInfo(p.getUser().getId(), room.playerCount()));
						infos.add(json);
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		player.sendMessage(outMsg.toString());
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
		log("joinRoom " + hostName);
		int error = -1;
		if(hostName == "") {
			int roomCount = Room.openRooms.size();
			if(roomCount > 0) {
				int index = random.nextInt(roomCount);
				Room room = (Room)((Room.openRooms.toArray())[index]);
				hostName = room.host.getUser().getId();
			} else {
				error = 2; // no room to join
			}
		}
		Player host = PlayerManager.playersSignedIn.get(hostName);
		ObjectNode outMsg = mapper.createObjectNode();
		if(error < 0) {
			if(host != null) {
				if(host.getState() == host.inRoomState && host.inRoomState.room != null) {
					Room room = host.inRoomState.room;
					if(room != null && room.admitsClients()) {
						outMsg.put("evt", FrontendEvents.InRoom.ordinal());
						outMsg.put("isHost", false);
						outMsg.put("isClient", true);
						room.addClient(player);
						player.sendMessage(outMsg.toString());
						//player.setState(player.inRoomState); //in room script
					} else {
						error = 3; // room does not admit clients
					}
				} else {
					error = 0; //host is not in a room
				}
			} else {
				error = 1; //no host
			}
		}
		
		if(error >= 0) {
			log("sending error");
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
