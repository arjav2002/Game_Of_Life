package com.arjav.gameoflife.client.game.ui;

import java.util.ArrayList;
import java.util.Scanner;

import com.arjav.gameoflife.client.game.Camera;
import com.arjav.gameoflife.client.game.Game;
import com.arjav.gameoflife.client.game.Type;
import com.arjav.gameoflife.client.game.entities.Building;
import com.arjav.gameoflife.client.game.entities.BuildingType;
import com.arjav.gameoflife.client.game.entities.Player;
import com.arjav.gameoflife.client.game.entities.Tile;
import com.arjav.gameoflife.client.game.entities.TileType;
import com.arjav.gameoflife.client.net.ClientConnect;
import com.arjav.gameoflife.maths.Matrix4f;
import com.arjav.gameoflife.maths.Vector3f;
import com.arjav.gameoflife.server.BuildingPacket;
import com.arjav.gameoflife.server.PlayerPacket;

public class Lobby extends GameState {

	private ArrayList<Building> buildings;
	private ArrayList<Tile> tiles;
	private Player[] players;
	private String[] gameplayEvents;
	private int nCurrentPlayers;
	private int nCurrentGameplayEvents;
	private int leftEnd, rightEnd; // part that is inside fort
	
	private final int BUILDING_WIDTH, BUILDING_HEIGHT;
	private final int TILE_WIDTH, TILE_HEIGHT;
	private final int PADDING;
	private final int LAND_LOC;
	private final int MAX_PLAYERS = 10;
	private final int MAX_GAMEPLAY_EVENTS = 15;
	private Player player;
	private ClientConnect connect;
	
	public Lobby(Game game, String vertexShader, String fragmentShader, ClientConnect connect, Player player) {
		super(game, vertexShader, fragmentShader);
		BUILDING_WIDTH = game.getWidth()/5;
		BUILDING_HEIGHT = game.getHeight()/3;
		TILE_WIDTH = TILE_HEIGHT = BUILDING_WIDTH/2;
		LAND_LOC = 3*game.getHeight()/4;
		PADDING = BUILDING_WIDTH/10;
		buildings = new ArrayList<Building>();
		players = new Player[MAX_PLAYERS];
		for(int i = 0; i < MAX_PLAYERS; i++) {
			players[i] = null;
		}
		gameplayEvents = new String[MAX_GAMEPLAY_EVENTS];
		tiles = new ArrayList<Tile>();
		this.player = player;
		players[0] = player;
		nCurrentPlayers = 1;
		this.connect = connect;
		nCurrentGameplayEvents = 0;
		
		connect.sendMessage("GetWorld");
		initWorld();
		
		game.getCamera().setEntityToFollow(player);
	}

	@Override
	public void render(Camera camera) {
		shader.enable();
		shader.setUniformMat4f("camera_matrix", camera.getViewMatrix());
		for(Building building : buildings) {
			building.render(shader, camera);
		}
		for(int i = 0; i < MAX_PLAYERS; i++) if(players[i] != null) players[i].render(shader, camera);
		for(Tile t : tiles) t.render(shader, camera);
		
		shader.disable();
	}

	@Override
	public void tick() {	
		connect.sendMessage("Tick");
		
		nCurrentGameplayEvents = 0;
		while(true) {
			String msg = connect.getMessage();
			if(msg.equals("END")) break;
			gameplayEvents[nCurrentGameplayEvents++] = msg;
			System.out.println(msg);
		}
		
		removeLeavingPlayers();
		addJoiningPlayers();
		
		nCurrentPlayers = Integer.parseInt(connect.getMessage());
		for(int i = 0; i < nCurrentPlayers; i++) {
			PlayerPacket pp = (PlayerPacket) connect.getObject();
			if(pp.getName().equals(player.getName())) continue;
			updatePlayerEntry(pp);
		}
				
		player.tick(game.getCamera(), tiles);

		for(int i = 0; i < MAX_PLAYERS; i++) {
			if(players[i] != null) connect.sendObject(players[i].getPlayerPacket());
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
			BuildingPacket bp = (BuildingPacket) connect.getObject();;
			buildings.add(new Building(getTexture(bp.getType()), new Vector3f(i*(BUILDING_WIDTH + PADDING), LAND_LOC-BUILDING_HEIGHT, 1.0f), BUILDING_WIDTH, BUILDING_HEIGHT, bp.getType(), bp.getZombies(), bp.getSupplies()));
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
		
		int nPlayers = Integer.parseInt(connect.getMessage());
		for(int i = 1; i < nPlayers+1; i++) {
			PlayerPacket pp =  (PlayerPacket) connect.getObject();
			Player p = new Player(pp.getX(), pp.getY(), pp.getName(), pp.getType());
			p.velX = pp.getVelX();
			p.velY = pp.getVelY();
			p.init();
			players[i] = p;
		}
		
		sc.close();
	}
	
	private void removeLeavingPlayers() {
		for(int j = 0; j < nCurrentGameplayEvents; j++) {
			String event = gameplayEvents[j];
			if(event.endsWith("left")) {
				String name = event.split(" ")[0];
				for(int i = 0; i < MAX_PLAYERS; i++) {
					Player p = players[i];
					if(p != null && p.getName().equals(name)) {
						players[i] = null;
					}
				}
			}
		}
	}
	
	private void addJoiningPlayers() {
		for(int j = 0; j < nCurrentGameplayEvents; j++) {
			String event = gameplayEvents[j];
			if(event.endsWith("joined")) {
				String name = event.split(" ")[0];
				String type = event.split(" ")[1];
				for(int i = 0; i < MAX_PLAYERS; i++) {
					if(players[i] == null) {
						Player p = new Player(0, 0, name, Type.valueOf(type));
						p.init();
						players[i] = p;
					}
				}
			}
		}
	}
	
	private void updatePlayerEntry(PlayerPacket pp) {
		for(int i = 0; i < MAX_PLAYERS; i++) {
			Player p = players[i];
			if(p != null && pp.getName().equals(p.getName())) {
				p.setX(pp.getX());
				p.setY(pp.getY());
				p.velX = pp.getVelX();
				p.velY = pp.getVelY();
				p.setShooting(pp.isShooting());
				return;
			}
		}
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

	
	public Player getPlayer() {
		return player;
	}
}
