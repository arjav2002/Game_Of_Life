package com.arjav.gameoflife.client.game.entities;

import com.arjav.gameoflife.maths.Vector3f;
import com.arjav.gameoflife.client.game.Type;

public class Player extends Entity {
	
	public static final int WIDTH = 64, HEIGHT = 128;

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
	
	public void setX(int x) {
		position.x = x;
	}
	
	public void setY(int y) {
		position.y = y;
	}
	
	public static String getTexture(Type soldierType) {
		switch(soldierType) {
		case juggernaut:
			return "/juggernaut_body.png";
		case medic:
			return "/medic_body.png";
		case sniper:
			return "/sniper_body.png";
		}
		return "NULL TEXTURE";
	}
}
