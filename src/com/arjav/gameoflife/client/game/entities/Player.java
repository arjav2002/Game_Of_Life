package com.arjav.gameoflife.client.game.entities;

import java.util.ArrayList;

import com.arjav.gameoflife.client.game.Camera;
import com.arjav.gameoflife.client.game.Type;
import com.arjav.gameoflife.client.game.graphics.Shader;
import com.arjav.gameoflife.client.game.graphics.Texture;
import com.arjav.gameoflife.maths.Vector3f;
import com.arjav.gameoflife.server.PlayerPacket;

public class Player extends Entity {
	
	public static final int WIDTH = 64, HEIGHT = 128;

	private Type type;
	private String name;
	public float velX = 0, velY = 0;
	private final float gravity = 2.0f;
	private final float maxVelY = 20.0f;
	private Texture leftHeadingTexture, rightHeadingTexture;
	private boolean shooting; // boolean for if bullet left the gun in the last tick
	
	public Player(int x, int y, String name, Type type) {
		super(getTexture(type), new Vector3f(x, y, 1.0f), WIDTH, HEIGHT);
		this.type = type;
		this.name = name;
		this.shooting = false;
		leftHeadingTexture = new Texture("/" + type.toString() + "_body_left.png");
	}
	
	@Override
	public void init() {
		super.init();
		rightHeadingTexture = model.getTexture();
	}
	
	@Override
	public void render(Shader shader, Camera camera) {
		if(velX > 0) model.setTexture(rightHeadingTexture);
		else if(velX < 0) model.setTexture(leftHeadingTexture);
		super.render(shader, camera);
	}
	
	public void tick(Camera camera, ArrayList<Tile> tiles) {
		velY += gravity;
		if(velY >= maxVelY) velY = maxVelY;
		position.x += velX;
		position.y += velY;
		for(Tile tile : tiles) {
			if(camera.isInBounds(tile.getPosition())) {
				if(getBoundsBottom().intersects(tile.getBoundsTop())) {
					velY = 0;
					position.y = tile.getPosition().y - HEIGHT;
				}
			}
		}
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
	
	public int getX() {
		return (int) position.x;
	}
	
	public int getY() {
		return (int) position.y;
	}
	
	public void setX(int x) {
		this.position.x = x;
	}
	
	public void setY(int y) {
		this.position.y = y;
	}
	
	public void setShooting(boolean shooting) {
		this.shooting = shooting;
	}
	
	public boolean hasShot() {
		return shooting;
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
	
	public PlayerPacket getPlayerPacket() {
		return new PlayerPacket((int)position.x, (int)position.y, name, type, velX, velY, shooting);
	}
}
