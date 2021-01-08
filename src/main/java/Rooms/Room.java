package Rooms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import Players.Player;


public class Room {
	
	public static HashSet<Room> openRooms = new HashSet<Room>();
	
	private static AtomicInteger roomCount = new AtomicInteger(0);
	private int id;
	public int level;
	
	public Player host;
	private HashSet<Player> clients;
	
	private boolean playing = false;
	
	private final int maxClients = 3;
	
	public Room(Player host, int level) {
		id = roomCount.incrementAndGet();
		this.host = host;
		this.level = level;
		host.inRoomState.room = this;
		host.inRoomState.isHost = true;
		host.inRoomState.isClient = false;
		this.clients = new HashSet<Player>();
		playing = false;
		openRooms.add(this);
		log("created by " + host.getUser().getId() + " level " + level);
	}
	
	public int playerCount() {
		return clients.size()+1;
	}
	
	public boolean admitsClients() {
		return !playing && clients.size() < maxClients;
	}
	
	public void addClient(Player client) {
		if(admitsClients()) {
			if(client != null) {
				client.setState(client.inRoomState);
				client.inRoomState.room = this;
				client.inRoomState.isHost = false;
				client.inRoomState.isClient = true;
				
				//SEND INFO
				client.inRoomState.addPlayer(host);
				for(Player p : clients) {
					client.inRoomState.addPlayer(p);
					p.inRoomState.addPlayer(client);
				}
				host.inRoomState.addPlayer(client);
				// 
				
				clients.add(client);
				
				if(!admitsClients()) {
					openRooms.remove(this);
				}
			}
		}
		else if(client != null) {
			log("could not add " + client.getUser().getId() + ", room does not admit clients");
		}
	}
	
	public void spawnPlayer(Player player, ObjectNode outMsg) {
		for(Player p : clients) {
			if(p != player) {
				log("sending spawn event to " + p.getUser().getId());
				p.sendMessage(outMsg.toString());
			}
		}
		if(host != player) {
			host.sendMessage(outMsg.toString());
		}
	}
	
	public void removeClient(Player client, boolean error, boolean disconnected) {
		if(client!=null) {
			log("added client " + client.getUser().getId());
			client.inRoomState.room = null;
			clients.remove(client);
			client.inRoomState.isClient = false;
			client.inRoomState.isHost = false;
			if(client.getState() == client.inRoomState) {
				if(error) {
					client.inRoomState.sendError();
				} else if(!disconnected) {
					client.inRoomState.exit();
				}
			}
			
			if(disconnected && !error) {
				for(Player p : clients) {
					p.inRoomState.removePlayer(client);
				}
				host.inRoomState.removePlayer(client);
			}
			
			if(admitsClients()) openRooms.add(this);
		}
	}
	
	public void removeHost() {
		log("remove host " + host.getUser().getId());
		if(host != null) {
			host.setState(host.signedInState);
			host.inRoomState.room = null;
			host = null;
		}
		for(Player c : clients) {
			removeClient(c, true, false);
		}
		openRooms.remove(this);
	}
	
	public void propagateInfo(String outMsg, Player player) {
		if(player.inRoomState.isHost) {
			for(Player c : clients) {
				c.sendMessage(outMsg);
			}
		} else if (player.inRoomState.isClient) {
			host.sendMessage(outMsg);
		}
	}
	
	private static boolean DEBUG_MODE = true;
	private void log(String msg) {
		if (DEBUG_MODE) {
			System.out.println("[ROOM " + id + "] " + msg);
		}
	}
	
}
