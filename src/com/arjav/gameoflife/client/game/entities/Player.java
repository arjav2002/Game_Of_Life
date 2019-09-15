package com.arjav.gameoflife.client.game.entities;

import com.arjav.gameoflife.client.game.Camera;
import com.arjav.gameoflife.client.game.Type;
import com.arjav.gameoflife.client.game.graphics.Shader;
import com.arjav.gameoflife.client.game.graphics.Texture;
import com.arjav.gameoflife.maths.Vector3f;

public class Player extends Entity {
	
	public static final int WIDTH = 64, HEIGHT = 128;

	private Type type;
	private String name;
	private float velX = 0, velY = 0;
	private final float gravity = 6.0f;
	private Texture leftHeadingTexture, rightHeadingTexture;
	
	public Player(String texturePath, int x, int y, String name, Type type) {
		super(texturePath, new Vector3f(x, y, 1.0f), WIDTH, HEIGHT);
		this.type = type;
		this.name = name;
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
	
	public void tick() {
		//velY += gravity;
		position.x += velX;
		position.y += velY;
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
		position.x = x;
	}
	
	public void setY(int y) {
		position.y = y;
	}
	
	public void setVelX(float velX) {
		this.velX = velX;
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
