package com.arjav.gameoflife.server;

import com.arjav.gameoflife.client.game.Type;

public class PlayerRecord {

	private int x, y;
	private String name;
	private Type type;
	
	public PlayerRecord(int x, int y, String name, Type type) {
		this.x = x;
		this.y = y;
		this.name = name;
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}
	
	public void setType(Type ty) {
		this.type = ty;
	}
	
	public String getName() {
		return name;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
}
