package com.arjav.gameoflife.client.game.ui;

import com.arjav.gameoflife.client.game.Camera;
import com.arjav.gameoflife.client.game.Game;
import com.arjav.gameoflife.client.game.graphics.Shader;
import com.arjav.gameoflife.maths.Matrix4f;

public abstract class GameState {

	protected Game game;
	protected Shader shader;
	
	protected GameState(Game game, String vertexShader, String fragmentShader) {
		this.game = game;
		shader = new Shader(vertexShader, fragmentShader);
	}
	
	public abstract void render(Camera camera);
	
	public abstract void tick();
	
	protected void init(Matrix4f prMatrix) {
		shader.setUniformMat4f("pr_matrix", prMatrix);
	}
	
}
