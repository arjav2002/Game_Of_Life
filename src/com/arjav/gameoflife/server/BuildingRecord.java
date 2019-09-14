package com.arjav.gameoflife.server;

import java.io.Serializable;

import com.arjav.gameoflife.client.game.entities.BuildingType;

public class BuildingRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5629433613409634620L;
	private BuildingType type;
	private int nZombies, nSupplies;
	
	public BuildingRecord(BuildingType type, int nZombies, int nSupplies) {
		this.type = type;
		this.nZombies = nZombies;
		this.nSupplies = nSupplies;
	}
	
	public BuildingType getType() {
		return type;
	}
	
	public int getZombies() {
		return nZombies;
	}
	
	public int getSupplies() {
		return nSupplies;
	}
	
}
