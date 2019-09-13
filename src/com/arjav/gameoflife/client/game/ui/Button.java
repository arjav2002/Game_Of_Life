package com.arjav.gameoflife.client.game.ui;

import com.arjav.gameoflife.client.game.entities.Entity;
import com.arjav.gameoflife.client.game.graphics.Model;
import com.arjav.gameoflife.maths.Matrix4f;
import com.arjav.gameoflife.maths.Vector3f;

public class Button extends Entity {
	
	public Button(String texturePath, int x, int y, int width, int height) {
		super(texturePath, new Vector3f(x, y, 1.0f), width, height);
	}
	
	public void init(Matrix4f pr_matrix, Matrix4f camera_matrix, Matrix4f model_matrix) {
		model.init();
	}
	
	public boolean isInBounds(int mx, int my) {
		return mx > position.x && mx < position.x + width && my > position.y && my < position.y + height;
	}
	
	public Model getModel() {
		return model;
	}

}
