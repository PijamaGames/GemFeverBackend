package Players;

import java.util.HashSet;
import java.util.Set;

public class PlayerManager {
	public static Set<Player> players= new HashSet<Player>();
	
	private final static boolean DEBUG = true;
	private static void log(String str) {
		if(DEBUG) {
			System.out.println("[PLAYER MANAGER] " + str);
		}
	}
	
	public static void Add(Player player) {
		players.add(player);
		log("New player: " + player.playerId);
		log("Player count: " + (DEBUG ? players.size() : 0));
	}
	
	public static void Remove(Player player) {
		players.remove(player);
		String name = player.user != null ? player.user.getId() : "unknown";
		log("Removed player " + player.playerId + " User: " + name);
		log("Player count: " + (DEBUG ? players.size() : 0));
	}
}
