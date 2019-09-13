package com.arjav.gameoflife.client.game;

import com.arjav.gameoflife.client.game.entities.Entity;
import com.arjav.gameoflife.maths.Matrix4f;

public class Camera {

	private Entity toFollow;
	private Matrix4f viewMatrix;
	
	public Camera(Entity toFollow) {
		this.toFollow = toFollow;
		viewMatrix = new Matrix4f();
		viewMatrix.identity();
	}
	
	public void tick() {
		if(toFollow != null) viewMatrix.translate(toFollow.getPosition());
	}
	
	public Matrix4f getViewMatrix() {
		return viewMatrix;
	}
	
	public void setEntityToFollow(Entity toFollow) {
		this.toFollow = toFollow;
	}
	
}
