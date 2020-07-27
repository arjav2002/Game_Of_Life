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
	
	public PlayerPacket(int x, int y, String name, Type type) {
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
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
}
