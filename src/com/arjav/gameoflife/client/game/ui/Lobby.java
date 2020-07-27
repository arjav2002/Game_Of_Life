package com.arjav.gameoflife.client.game.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.arjav.gameoflife.client.game.Camera;
import com.arjav.gameoflife.client.game.Game;
import com.arjav.gameoflife.client.game.entities.Building;
import com.arjav.gameoflife.client.game.entities.BuildingType;
import com.arjav.gameoflife.client.game.entities.Player;
import com.arjav.gameoflife.client.game.entities.Tile;
import com.arjav.gameoflife.client.game.entities.TileType;
import com.arjav.gameoflife.client.net.Connect;
import com.arjav.gameoflife.maths.Matrix4f;
import com.arjav.gameoflife.maths.Vector3f;
import com.arjav.gameoflife.server.BuildingPacket;
import com.arjav.gameoflife.server.PlayerPacket;

public class Lobby extends GameState {

	private ArrayList<Building> buildings;
	private ArrayList<Tile> tiles;
	private ArrayList<Player> players;
	private int leftEnd, rightEnd; // part that is inside fort
	private Map<String, PlayerPacket> otherPlayersInLobby;
	private Map<String, Boolean> otherPlayersTicked;
	
	private final int BUILDING_WIDTH, BUILDING_HEIGHT;
	private final int TILE_WIDTH, TILE_HEIGHT;
	private final int PADDING;
	private final int LAND_LOC;
	private Player player;
	private PlayerPacket playerRecord;
	private Connect connect;
	
	public Lobby(Game game, String vertexShader, String fragmentShader, Connect connect, Player player) {
		super(game, vertexShader, fragmentShader);
		BUILDING_WIDTH = game.getWidth()/5;
		BUILDING_HEIGHT = game.getHeight()/3;
		TILE_WIDTH = TILE_HEIGHT = BUILDING_WIDTH/2;
		LAND_LOC = 3*game.getHeight()/4;
		PADDING = BUILDING_WIDTH/10;
		buildings = new ArrayList<Building>();
		otherPlayersInLobby = new HashMap<String, PlayerPacket>();
		otherPlayersTicked = new HashMap<String, Boolean>();
		players = new ArrayList<Player>();
		tiles = new ArrayList<Tile>();
		this.player = player;
		players.add(player);
		this.connect = connect;
		
		connect.sendMessage("GW");
		initWorld();
		
		playerRecord = new PlayerPacket(player.getX(), player.getY(), player.getName(), player.getType());
		game.getCamera().setEntityToFollow(player);
	}

	@Override
	public void render(Camera camera) {
		shader.enable();
		shader.setUniformMat4f("camera_matrix", camera.getViewMatrix());
		for(Building building : buildings) {
			building.render(shader, camera);
		}
		for(Player p : players) p.render(shader, camera);
		for(Tile t : tiles) t.render(shader, camera);
		
		shader.disable();
	}

	@Override
	public void tick() {
		connect.sendMessage("TICK");
		playerRecord.setX(player.getX());
		playerRecord.setY(player.getY());
		connect.sendObject(playerRecord);
		player.tick(game.getCamera(), tiles);
		// send your own state
		int toProcess = Integer.parseInt(connect.getMessage());
		for(Player p : players) {
			if(p != player) otherPlayersTicked.put(p.getName(), false);
		}
		for(int i = 0; i < toProcess; i++) {
			PlayerPacket recordReceived = (PlayerPacket) connect.getObject();
			Player p = getPlayer(recordReceived.getName());
			if(p == null) p = createNewPlayer(recordReceived);
			p.setX(recordReceived.getX());
			p.setY(recordReceived.getY());
			otherPlayersTicked.put(p.getName(), true);
		}
		cleanUP();
		// update others' states
	}
	
	private void cleanUP() {
		ArrayList<String> toRemove = new ArrayList<String>();
		for(Player p : players) {
			if(p != player && !otherPlayersTicked.get(p.getName())) toRemove.add(p.getName());
		}
		for(String name : toRemove) {
			players.remove(getPlayer(name));
			otherPlayersTicked.remove(name);
			otherPlayersInLobby.remove(name);
		}
	}
	
	public void init(Matrix4f prMatrix) {
		super.init(prMatrix);
		for(Building building : buildings) {
			building.init();
		}
		player.init();
		for(Tile t : tiles) t.init();
	}
	
	private void initWorld() {
		Scanner sc = new Scanner(connect.getMessage());
		int nBuildingsToRead = sc.nextInt();
		leftEnd = sc.nextInt();
		rightEnd = sc.nextInt();
		for(int i = 0; i < nBuildingsToRead; i++) {
			BuildingPacket buildingRecord = (BuildingPacket) connect.getObject();
			buildings.add(new Building(getTexture(buildingRecord.getType()), new Vector3f(i*(BUILDING_WIDTH + PADDING), LAND_LOC-BUILDING_HEIGHT, 1.0f), BUILDING_WIDTH, BUILDING_HEIGHT, buildingRecord.getType(), buildingRecord.getZombies(), buildingRecord.getSupplies()));
			if(i == leftEnd) {
				player.setX(i*(BUILDING_WIDTH + PADDING)+TILE_WIDTH + 20);
				player.setY(LAND_LOC - Player.HEIGHT);
				for(int j = 0; j < 5; j++) {
					tiles.add(new Tile("/wall.png", new Vector3f(i*(BUILDING_WIDTH + PADDING) - TILE_WIDTH/2, LAND_LOC-(j+1)*TILE_HEIGHT, 1.0f), TILE_WIDTH, TILE_HEIGHT, TileType.wall));
				}
			}
			if(i == rightEnd) {
				for(int j = 0; j < 5; j++) {
					tiles.add(new Tile("/wall.png", new Vector3f(i*(BUILDING_WIDTH + PADDING) - TILE_WIDTH/2, LAND_LOC-(j+1)*TILE_HEIGHT, 1.0f), TILE_WIDTH, TILE_HEIGHT, TileType.wall));
				}
			}
		}
		for(int i = 0; i <= nBuildingsToRead*(BUILDING_WIDTH + PADDING); i += TILE_WIDTH) {
			tiles.add(new Tile("/mud.png", new Vector3f(i, LAND_LOC, 1.0f), TILE_WIDTH, TILE_HEIGHT, TileType.mud));
		}
		
		sc.close();
	}
	
	private Player createNewPlayer(PlayerPacket playerRecord) {
		Player p = new Player(Player.getTexture(playerRecord.getType()), playerRecord.getX(), playerRecord.getY(), playerRecord.getName(), playerRecord.getType());
		p.init();
		otherPlayersInLobby.put(playerRecord.getName(), playerRecord);
		players.add(p);
		otherPlayersTicked.put(playerRecord.getName(), false);
		return p;
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
	
	private Player getPlayer(String name) {
		for(int i = 0; i < players.size(); i++)
			if(players.get(i).getName().equals(name)) return players.get(i);
		return null;
	}
	
	public Player getPlayer() {
		return player;
	}
}
