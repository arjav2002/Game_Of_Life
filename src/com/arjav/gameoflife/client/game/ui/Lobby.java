package com.arjav.gameoflife.client.game.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.arjav.gameoflife.client.game.Camera;
import com.arjav.gameoflife.client.game.Game;
import com.arjav.gameoflife.client.game.Type;
import com.arjav.gameoflife.client.game.entities.Building;
import com.arjav.gameoflife.client.game.entities.BuildingType;
import com.arjav.gameoflife.client.game.entities.Player;
import com.arjav.gameoflife.client.net.Connect;
import com.arjav.gameoflife.maths.Matrix4f;
import com.arjav.gameoflife.maths.Vector3f;
import com.arjav.gameoflife.server.BuildingRecord;
import com.arjav.gameoflife.server.PlayerRecord;

public class Lobby extends GameState {

	private ArrayList<Building> buildings;
	private int leftEnd, rightEnd; // part that is inside fort
	private Map<String, PlayerRecord> playersInLobby;
	
	private final int BUILDING_WIDTH, BUILDING_HEIGHT;
	private final int PADDING;
	private final int LAND_LOC;
	private Player player;
	
	public Lobby(Game game, String vertexShader, String fragmentShader, Connect connect, Player player) {
		super(game, vertexShader, fragmentShader);
		BUILDING_WIDTH = game.getWidth()/5;
		BUILDING_HEIGHT = game.getHeight()/3;
		LAND_LOC = 3*game.getHeight()/4;
		PADDING = BUILDING_WIDTH/10;
		buildings = new ArrayList<Building>();
		playersInLobby = new HashMap<String, PlayerRecord>();
		this.player = player;
		
		connect.sendMessage("GW");
		String[] info = connect.getMessage().split(" ");
		int nBuildings = Integer.parseInt(info[0]);
		leftEnd = Integer.parseInt(info[1]);
		rightEnd = Integer.parseInt(info[2]);
		for(int i = 0; i < nBuildings; i++) {
			BuildingRecord br = (BuildingRecord) connect.getObject();
			buildings.add(new Building(getTexture(br.getType()), new Vector3f(i*(BUILDING_WIDTH + PADDING), LAND_LOC - BUILDING_HEIGHT, 1.0f), BUILDING_WIDTH, BUILDING_HEIGHT, br.getType(), br.getZombies(), br.getSupplies()));
		}
		player.setX(leftEnd*(BUILDING_WIDTH + PADDING) + PADDING);
		player.setY(LAND_LOC - Player.HEIGHT - 20);
		game.getCamera().setEntityToFollow(player);
	}

	@Override
	public void render(Camera camera) {
		shader.enable();
		shader.setUniformMat4f("camera_matrix", camera.getViewMatrix());
		for(Building building : buildings) building.render(shader);
		
		player.render(shader);
		
		shader.disable();
	}

	@Override
	public void tick() {
		player.tick();
	}
	
	public void init(Matrix4f prMatrix) {
		super.init(prMatrix);
		for(Building building : buildings) {
			building.init();
		}
		player.init();
	}

	private String getTexture(BuildingType type) {
		switch(type) {
		case hospital:
			return "/hospital.png";
		case police:
			return "/police.png";
		case farm:
			return "/farm.png";
		case apartment:
			return "/apartments.png";
		}
		return "NULL TEXTURE";
	}
	
}
