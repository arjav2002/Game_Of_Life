package com.arjav.gameoflife.client.game.ui;

import com.arjav.gameoflife.client.game.graphics.Model;
import com.arjav.gameoflife.maths.Matrix4f;

public class Button {
	
	private Model model;
	private int x, y, width, height;
	
	public Button(String vertexShader, String fragmentShader, String texturePath, int x, int y, int width, int height) {
		model = new Model(vertexShader, fragmentShader, texturePath, new float[] {
					x, y, 1.0f,
					x + width, y, 1.0f,
					x + width, y+height, 1.0f,
					x, y+height, 1.0f
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
				});
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void render() {
		model.render();
	}
	
	public void init(Matrix4f pr_matrix, Matrix4f trans_matrix) {
		model.init();
		model.getShader().setUniformMat4f("pr_matrix", pr_matrix);
		model.getShader().setUniformMat4f("trans_matrix", trans_matrix);
	}
	
	public boolean isInBounds(int mx, int my) {
		return mx > x && mx < x + width && my > y && my < y + height;
	}
	
	public Model getModel() {
		return model;
	}

}
