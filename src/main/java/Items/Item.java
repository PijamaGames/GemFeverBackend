package Items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Users.User;

public class Item {
	public String name;
	public int price;
	public ItemTier tier;
	public ItemType type;
	public boolean avaible;
	public int gems;
	
	public Item(String name, int price, int gems, boolean avaible, ItemTier tier, ItemType type) {
		super();
		this.name = name;
		this.price = price;
		this.tier = tier;
		this.type = type;
		this.avaible = avaible;
		this.gems = gems;
	}
	
	public void AddToUser(User user) {
		if(type == ItemType.gem) {
			user.setGems(user.getGems()+gems);
		} else {
			String[] original = new String[0];
			switch(type) {
				case hat: original = user.getItems_hats(); break;
				case frame: original = user.getItems_frames(); break;
				case face: original = user.getItems_faces(); break;
			}
			String[] newItems = new String[original.length+1];
			for(int i = 0; i < original.length; i++) newItems[i] = original[i];
			newItems[original.length] = name;
			switch(type) {
				case hat: user.setItems_hats(newItems); break;
				case frame: user.setItems_frames(newItems); break;
				case face: user.setItems_faces(newItems); break;
			}
		}
	}
}
