package Players;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PlayerState {

	protected static ObjectMapper mapper = new ObjectMapper();
	
	protected Player player;
	
	protected final boolean DEBUG = true;
	protected void log(String msg) {
		if(DEBUG) {
			System.out.println("[PLAYER "+player.playerId+"] " + msg);
		}
	}
	
	public PlayerState(Player player) {
		this.player = player;
	}
	
	public void handleMessage(JsonNode inMsg) {
		
	}
	
	protected void begin() {
		
	}
	
	protected void finish() {
		
	}
}
