package com.arjav.gameoflife.client.game.entities;

import com.arjav.gameoflife.maths.Vector3f;

public class Building extends Entity{
	
	private BuildingType buildingType;
	private int nZombies, nSupplies;
	
	public Building(String texturePath, Vector3f position, int width, int height, BuildingType buildingType, int nZombies, int nSupplies) {
		super(texturePath, position, width, height);
		this.buildingType = buildingType;
		this.nZombies = nZombies;
		this.nSupplies = nSupplies;
	}
	
	public BuildingType getBuildingType() {
		return buildingType;
	}
	
	public int getZombies() {
		return nZombies;
	}
	
	public int getSupplies() {
		return nSupplies;
	}

}
