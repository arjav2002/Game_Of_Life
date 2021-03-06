package com.arjav.gameoflife.server;

import com.arjav.gameoflife.client.game.Type;
import com.arjav.gameoflife.client.net.Client;

public class PlayerClient {
	
	private Client associatedClient;
	private String name;
	public Type type;

	public PlayerClient(String name, Type type, Client associatedClient) {
		this.name = name;
		this.type = type;
		this.associatedClient = associatedClient;
	}
	
	public Client getAssociatedClient() {
		return associatedClient;
	}
	
	public String getName() {
		return name;
	}
	
}
