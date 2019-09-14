package com.arjav.gameoflife.server;

import com.arjav.gameoflife.client.game.Type;

public class ServerPlayer extends PlayerRecord {
	
	private Client associatedClient;

	public ServerPlayer(int x, int y, String name, Type type, Client associatedClient) {
		super(x, y, name, type);
		this.associatedClient = associatedClient;
	}
	
	public Client getAssociatedClient() {
		return associatedClient;
	}
	
}
