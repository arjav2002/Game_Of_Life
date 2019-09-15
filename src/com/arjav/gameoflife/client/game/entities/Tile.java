package com.arjav.gameoflife.client.game.entities;

import java.awt.Rectangle;

import com.arjav.gameoflife.maths.Vector3f;

public class Tile extends Entity {

	private TileType type;
	
	public Tile(String texturePath, Vector3f position, int width, int height, TileType type) {
		super(texturePath, position, width, height);
		this.type = type;
	}
	
	public TileType getType() {
		return type;
	}
	
	public Rectangle getBoundsTop() {
		return new Rectangle((int)position.x+2, (int)position.y-2, getWidth()-4, 12);
	}
	
	public Rectangle getBoundsBottom() {
		return new Rectangle((int)position.x+2, (int)position.y+getHeight()-10, getWidth()-4, 12);
	}

	public Rectangle getBoundsLeft() {
		return new Rectangle((int)position.x-2, (int)position.y+2, 12, getHeight()-4);
	}

	public Rectangle getBoundsRight() {
		return new Rectangle((int)position.x-10+getWidth(), (int)position.y+2, 12, getHeight()-4);
	}


}
