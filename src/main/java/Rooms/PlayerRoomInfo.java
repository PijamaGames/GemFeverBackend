package Rooms;

public class PlayerRoomInfo {
	String player;
	int count;
	
	
	public PlayerRoomInfo(String player, int count) {
		super();
		this.player = player;
		this.count = count;
	}
	public String getPlayer() {
		return player;
	}
	public void setPlayer(String player) {
		this.player = player;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
}
