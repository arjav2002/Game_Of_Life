package com.arjav.gameoflife.server;

import java.io.Serializable;

import com.arjav.gameoflife.client.game.Type;

public class PlayerPacket implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6763329836897208741L;
	private int x, y;
	private String name;
	private Type type;
	private boolean shooting;
	private float velX, velY;
	
	public PlayerPacket(int x, int y, String name, Type type, float velX, float velY, boolean shooting) {
		this.x = x;
		this.y = y;
		this.name = name;
		this.type = type;
		this.velX = velX;
		this.velY = velY;
		this.shooting = shooting;
		
	}
	
	public Type getType() {
		return type;
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
	
	public boolean isShooting() {
		return shooting;
	}
	
	public float getVelX() {
		return velX;
	}
	
	public float getVelY() {
		return velY;
	}
}
