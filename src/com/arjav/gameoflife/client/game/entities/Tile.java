package com.arjav.gameoflife.client.game.entities;

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


}
