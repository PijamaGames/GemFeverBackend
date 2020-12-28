package com.gemFeverBackend;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Items.Item;
import Users.User;

public class GameEvent {
	public String name;
	public LocalDate start;
	public LocalDate end;
	
	public Item[] items;
	
	public String englishMsg;
	public String spanishMsg;
	
	public GameEvent(String name, LocalDate start, LocalDate end, Item[] items, String englishMsg, String spanishMsg) {
		super();
		this.name = name;
		this.start = start;
		this.end = end;
		this.items = items;
		this.englishMsg = englishMsg;
		this.spanishMsg = spanishMsg;
	}
	
	public void AddToUser(User user) {
		String[] originalEvents = user.getEventsAttended();
		String[] newEvents = new String[originalEvents.length+1];
		for(int i = 0; i < originalEvents.length; i++) newEvents[i] = originalEvents[i];
		newEvents[originalEvents.length] = this.name;
		user.setEventsAttended(newEvents);
		for(Item i : items) {
			i.AddToUser(user);
		}
	}
}
