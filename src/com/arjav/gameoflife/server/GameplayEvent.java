package com.arjav.gameoflife.server;

import java.util.ArrayList;
import java.util.HashMap;

public class GameplayEvent {

	private String event;
	private HashMap<String, Boolean> usersNotified;
	
	public GameplayEvent(String event, ArrayList<PlayerClient> players) {
		this.event = event;
		usersNotified = new HashMap<String, Boolean>();
		for(PlayerClient pc : players) {
			usersNotified.put(pc.getName(), false);
		}
	}
	
	public boolean isDone() {
		for(Boolean notified : usersNotified.values()) {
			if(!notified) return false;
		}
		return true;
	}
	
	public void notifiedUser(String name) {
		usersNotified.put(name, true);
	}
	
	public boolean haveNotifiedUser(String name) {
		return usersNotified.get(name);
	}
	
	public String getEventString() {
		return event;
	}
	
	public void removeUser(String name) {
		usersNotified.remove(name);
	}
	
	public void addUser(String name) {
		usersNotified.put(name, false);
	}
}
