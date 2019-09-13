package com.arjav.gameoflife.client.game.entities;

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
				position.x, position.y, position.z,
				position.x + width, position.y, position.z,
				position.x + width, position.y+height, position.z,
				position.x, position.y+height, position.z
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
	
	public void tick() {
		model_matrix.translate(position);
	}
	
	public void render(Shader shader) {
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
