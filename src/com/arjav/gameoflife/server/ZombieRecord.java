package com.arjav.gameoflife.server;

public class ZombieRecord {

	private int x, y, health;
	
	public ZombieRecord(int x, int y, int health) {
		this.x = x;
		this.y = y;
		this.health = health;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getHealth() {
		return health;
	}
	
}
