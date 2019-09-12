package com.arjav.gameoflife.client.game;

public class Player {
	
	public static final int WIDTH = 32, HEIGHT = 64;

	private int x, y;
	private Type type;
	private String name;
	
	public Player(int x, int y, String name, Type type) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.name = name;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Type getType() {
		return type;
	}
	
	public String getName() {
		return name;
	}
	
	public void setType(Type type) {
		this.type = type;
	}
}
