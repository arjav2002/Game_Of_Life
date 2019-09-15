package com.arjav.gameoflife.client.game.entities;

import com.arjav.gameoflife.client.game.Camera;
import com.arjav.gameoflife.client.game.graphics.Model;
import com.arjav.gameoflife.client.game.graphics.Shader;
import com.arjav.gameoflife.maths.Matrix4f;
import com.arjav.gameoflife.maths.Vector3f;

public class Entity {

	protected Model model;
	private Matrix4f model_matrix;
	protected Vector3f position;
	protected int width, height;
	
	protected Entity(Model model, Vector3f position, int width, int height) {
		this.model = model;
		model_matrix = new Matrix4f();
		model_matrix.identity();
		this.position = position;
		this.width = width;
		this.height = height;
	}
	
	protected Entity(String texturePath, Vector3f position, int width, int height) {
		this(new Model(texturePath, new float[] {
				0, 0, 0.0f,
				width, 0, 0.0f,
				width, height, 0.0f,
				0, height, 0.0f
			},
			new byte[] {
				0, 1, 3,
				1, 2, 3
			},
			new float[] {
				0.0f, 0.0f,
				1.0f, 0.0f,
				1.0f, 1.0f,
				0.0f, 1.0f	
			}), position, width, height);
	}
	
	public void init() {
		model.init();
	}
	
	public void render(Shader shader, Camera camera) {
		if(camera.isInBounds(position))
			model_matrix.translate(position);
			shader.setUniformMat4f("model_matrix", model_matrix);
			model.render();
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
}
