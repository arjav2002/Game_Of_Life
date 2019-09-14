package com.arjav.gameoflife.client.game;

import com.arjav.gameoflife.client.game.entities.Entity;
import com.arjav.gameoflife.maths.Matrix4f;
import com.arjav.gameoflife.maths.Vector3f;

public class Camera {

	private Entity toFollow;
	private Matrix4f viewMatrix;
	private Vector3f position;
	private Game game;
	
	public Camera(Game game, Entity toFollow) {
		this.game = game;
		this.toFollow = toFollow;
		viewMatrix = new Matrix4f();
		viewMatrix.identity();
		position = new Vector3f(0, 0, 0);
	}
	
	public void tick() {
		if(toFollow != null) {
			position.x = toFollow.getPosition().x - game.getWidth()/2;
			position.y = toFollow.getPosition().y - game.getHeight()/2;			
		}
	}
	
	public Matrix4f getViewMatrix() {
		viewMatrix.translate(position);
		return viewMatrix;
	}
	
	public void setEntityToFollow(Entity toFollow) {
		this.toFollow = toFollow;
	}
	
	// temp method
	public void setTranslate(int x) {
		viewMatrix.translate(new Vector3f(x, 0, 0));
	}
	
}
