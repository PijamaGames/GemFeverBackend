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
	private String avatar_face = "";
    private String avatar_hat = "";
    private String avatar_frame = "";
    
    private String[] friendRequests;
	private String[] friends;
	private String[] eventsAttended;

    private String[] items_faces;
    private String[] items_hats;
    private String[] items_frames;
    
    private boolean allowRequests;
    private boolean allowInvitations;
    
    public User() {
    	this.id = "";
		this.password = "";
		this.avatar_bodyType = 0;
		this.avatar_skinTone = 0;
		this.avatar_color = 0;
		this.avatar_face = "";
		this.avatar_hat = "";
		this.avatar_frame = "";
		
		this.friendRequests = new String[0];
		this.friends = new String[0];
		this.eventsAttended = new String[0];
		this.items_faces = new String[] {};
		this.items_hats = new String[] {};
		this.items_frames = new String[] {};
		
		this.allowRequests = true;
		this.allowInvitations = true;
    }
    
    public User(String id, String password) {
    	this.id = id;
		this.password = password;
		this.avatar_bodyType = 0;
		this.avatar_skinTone = 0;
		this.avatar_color = 0;
		this.avatar_face = "";
		this.avatar_hat = "";
		this.avatar_frame = "";
		
		this.friendRequests = new String[0];
		this.friends = new String[0];
		this.eventsAttended = new String[0];
		this.items_faces = new String[] {};
		this.items_hats = new String[] {};
		this.items_frames = new String[] {};
		
		this.allowRequests = true;
		this.allowInvitations = true;
    }
    
	public User(String id, String password, int avatar_bodyType, int avatar_skinTone, int avatar_color,
			String avatar_face, String avatar_hat, String avatar_frame) {
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
		this.items_faces = new String[] {};
		this.items_hats = new String[] {};
		this.items_frames = new String[] {};
		
		this.allowInvitations = true;
		this.allowRequests = true;
	}

	public User(String id, String password, int gems, int avatar_bodyType, int avatar_skinTone, int avatar_color,
			String avatar_face, String avatar_hat, String avatar_frame, String[] friendRequests, String[] friends,
			String[] eventsAttended, String[] items_faces, String[] items_hats, String[] items_frames, boolean allowRequests,
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

	public String getAvatar_face() {
		return avatar_face;
	}

	public void setAvatar_face(String avatar_face) {
		this.avatar_face = avatar_face;
	}

	public String getAvatar_hat() {
		return avatar_hat;
	}

	public void setAvatar_hat(String avatar_hat) {
		this.avatar_hat = avatar_hat;
	}

	public String getAvatar_frame() {
		return avatar_frame;
	}

	public void setAvatar_frame(String avatar_frame) {
		this.avatar_frame = avatar_frame;
	}

	public String[] getItems_faces() {
		return items_faces;
	}

	public void setItems_faces(String[] items_faces) {
		this.items_faces = items_faces;
	}

	public String[] getItems_hats() {
		return items_hats;
	}

	public void setItems_hats(String[] items_hats) {
		this.items_hats = items_hats;
	}

	public String[] getItems_frames() {
		return items_frames;
	}

	public void setItems_frames(String[] items_frames) {
		this.items_frames = items_frames;
	}
	
}
