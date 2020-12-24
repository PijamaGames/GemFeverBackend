package Users;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.gemFeverBackend.GameHandler;

@Document(collection="Users")
public class User {
	
	@Id
	private String id;
	private String password;
	private int gems;
	
	private int avatar_bodyType = 0;
	private int avatar_skinTone = 0;
	private int avatar_color = 0;
	private int avatar_face = 0;
    private int avatar_hat = 0;
    private int avatar_frame = 0;
    
    private String[] friendRequests;
	private String[] friends;
	private String[] eventsAttended;

    private int[] items_faces;
    private int[] items_hats;
    private int[] items_frames;
    
    private boolean allowRequests;
    private boolean allowInvitations;
    
    public User() {
    	this.id = "";
		this.password = "";
		this.avatar_bodyType = 0;
		this.avatar_skinTone = 0;
		this.avatar_color = 0;
		this.avatar_face = 0;
		this.avatar_hat = 0;
		this.avatar_frame = 0;
		
		this.friendRequests = new String[0];
		this.friends = new String[0];
		this.eventsAttended = new String[0];
		this.items_faces = new int[] {0,1,2,3};
		this.items_hats = new int[] {0,1,2,3};
		this.items_frames = new int[] {0,1,2,3};
		
		this.allowRequests = true;
		this.allowInvitations = true;
    }
    
	public User(String id, String password, int avatar_bodyType, int avatar_skinTone, int avatar_color,
			int avatar_face, int avatar_hat, int avatar_frame) {
		this.id = id;
		this.password = password;
		this.avatar_bodyType = avatar_bodyType;
		this.avatar_skinTone = avatar_skinTone;
		this.avatar_color = avatar_color;
		this.avatar_face = avatar_face;
		this.avatar_hat = avatar_hat;
		this.avatar_frame = avatar_frame;
		
		this.friendRequests = new String[0];
		this.friends = new String[0];
		this.eventsAttended = new String[0];
		this.items_faces = new int[] {0,1,2,3};
		this.items_hats = new int[] {0,1,2,3};
		this.items_frames = new int[] {0,1,2,3};
		
		this.allowInvitations = true;
		this.allowRequests = true;
	}

	public User(String id, String password, int gems, int avatar_bodyType, int avatar_skinTone, int avatar_color,
			int avatar_face, int avatar_hat, int avatar_frame, String[] friendRequests, String[] friends,
			String[] eventsAttended, int[] items_faces, int[] items_hats, int[] items_frames, boolean allowRequests,
			boolean allowInvitations) {
		super();
		this.id = id;
		this.password = password;
		this.gems = gems;
		this.avatar_bodyType = avatar_bodyType;
		this.avatar_skinTone = avatar_skinTone;
		this.avatar_color = avatar_color;
		this.avatar_face = avatar_face;
		this.avatar_hat = avatar_hat;
		this.avatar_frame = avatar_frame;
		this.friendRequests = friendRequests;
		this.friends = friends;
		this.eventsAttended = eventsAttended;
		this.items_faces = items_faces;
		this.items_hats = items_hats;
		this.items_frames = items_frames;
		this.allowRequests = allowRequests;
		this.allowInvitations = allowInvitations;
	}

	public String[] getEventsAttended() {
		return eventsAttended;
	}

	public void setEventsAttended(String[] eventsAttended) {
		this.eventsAttended = eventsAttended;
	}

	public boolean isAllowRequests() {
		return allowRequests;
	}

	public void setAllowRequests(boolean allowRequests) {
		this.allowRequests = allowRequests;
	}

	public boolean isAllowInvitations() {
		return allowInvitations;
	}

	public void setAllowInvitations(boolean allowInvitations) {
		this.allowInvitations = allowInvitations;
	}

	public void save() {
		GameHandler.repo.save(this);
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getGems() {
		return gems;
	}
	public void setGems(int gems) {
		this.gems = gems;
	}
	public String[] getFriendRequests() {
		return friendRequests;
	}
	public void setFriendRequests(String[] friendRequests) {
		this.friendRequests = friendRequests;
	}
	public String[] getFriends() {
		return friends;
	}
	public void setFriends(String[] friends) {
		this.friends = friends;
	}
	public int getAvatar_bodyType() {
		return avatar_bodyType;
	}
	public void setAvatar_bodyType(int avatar_bodyType) {
		this.avatar_bodyType = avatar_bodyType;
	}
	public int getAvatar_skinTone() {
		return avatar_skinTone;
	}
	public void setAvatar_skinTone(int avatar_skinTone) {
		this.avatar_skinTone = avatar_skinTone;
	}
	public int getAvatar_color() {
		return avatar_color;
	}
	public void setAvatar_color(int avatar_color) {
		this.avatar_color = avatar_color;
	}
	public int getAvatar_face() {
		return avatar_face;
	}
	public void setAvatar_face(int avatar_face) {
		this.avatar_face = avatar_face;
	}
	public int getAvatar_hat() {
		return avatar_hat;
	}
	public void setAvatar_hat(int avatar_hat) {
		this.avatar_hat = avatar_hat;
	}
	public int getAvatar_frame() {
		return avatar_frame;
	}
	public void setAvatar_frame(int avatar_frame) {
		this.avatar_frame = avatar_frame;
	}
	public int[] getItems_faces() {
		return items_faces;
	}
	public void setItems_faces(int[] items_faces) {
		this.items_faces = items_faces;
	}
	public int[] getItems_hats() {
		return items_hats;
	}
	public void setItems_hats(int[] items_hats) {
		this.items_hats = items_hats;
	}
	public int[] getItems_frames() {
		return items_frames;
	}
	public void setItems_frames(int[] items_frames) {
		this.items_frames = items_frames;
	}
}
