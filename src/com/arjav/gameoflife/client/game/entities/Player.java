package com.arjav.gameoflife.client.game.entities;

import com.arjav.gameoflife.maths.Vector3f;
import com.arjav.gameoflife.client.game.Type;

public class Player extends Entity {
	
	public static final int WIDTH = 32, HEIGHT = 64;

	private Type type;
	private String name;
	
	public Player(String texturePath, int x, int y, String name, Type type) {
		super(texturePath, new Vector3f(x, y, 1.0f), WIDTH, HEIGHT);
		this.type = type;
		this.name = name;
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
