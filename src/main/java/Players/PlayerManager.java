package Players;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import Users.User;

public class PlayerManager {
	public static Set<Player> players= new HashSet<Player>();
	public static HashMap<String, User> usersInUse = new HashMap<String, User>();
	public static HashMap<String, Player> playersSignedIn = new HashMap<String, Player>();
	
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
		User user = player.getUser();
		String name = user != null ? user.getId() : "unknown";
		log("Removed player " + player.playerId + " User: " + name);
		log("Player count: " + (DEBUG ? players.size() : 0));
	}
	
	public static void AddUser(Player player, User user) {
		usersInUse.put(user.getId(), user);
		playersSignedIn.put(user.getId(), player);
	}
	
	public static void RemoveUser(User user) {
		usersInUse.remove(user.getId());
		playersSignedIn.remove(user.getId());
	}
}
