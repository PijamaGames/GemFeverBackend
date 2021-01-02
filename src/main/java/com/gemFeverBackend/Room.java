package com.gemFeverBackend;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import Players.Player;


public class Room {
	private static AtomicInteger roomCount = new AtomicInteger(0);
	private int id;
	
	private Player host;
	private HashSet<Player> clients;
	
	public Room(Player host) {
		id = roomCount.incrementAndGet();
		this.host = host;
		host.inRoomState.room = this;
		host.inRoomState.isHost = true;
		host.inRoomState.isClient = false;
		this.clients = new HashSet<Player>();
	}
	
	public void addClient(Player client) {
		if(client != null) {
			client.inRoomState.room = this;
			clients.add(client);
			client.inRoomState.isHost = false;
			client.inRoomState.isClient = true;
		}
	}
	
	public void removeClient(Player client, boolean error, boolean disconnected) {
		if(client!=null) {
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
		}
	}
	
	public void removeHost() {
		if(host != null) {
			
			host.setState(host.signedInState);
			host = null;
		}
		for(Player c : clients) {
			removeClient(c, true, false);
		}
	}
	
	private static boolean DEBUG_MODE = true;
	private void log(String msg) {
		if (DEBUG_MODE) {
			System.out.println("[ROOM " + id + "] " + msg);
		}
	}
	
}
