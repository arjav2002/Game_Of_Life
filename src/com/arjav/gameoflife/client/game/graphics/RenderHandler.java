package com.arjav.gameoflife.client.game.graphics;

import com.arjav.gameoflife.client.game.Game;
import com.arjav.gameoflife.client.game.ui.Button;
import com.arjav.gameoflife.maths.Matrix4f;

public class RenderHandler {
	
	private Game game;
	
	public RenderHandler(Game game) {
		this.game = game;
	}
	
	public void render() {
		
		switch(game.getState()) {
		case typeChoose:
			game.getTypeChooseScreen().render(game.getCamera().getViewMatrix());
			break;
		case lobby:
			break;
		case onTask:
			break;
		default:
			break;
		}
	}
	
	public void init() {
		Matrix4f pr_matrix = Matrix4f.orthographic(0, game.getWidth(), game.getWidth() * 9.0f / 16.0f, 0, -1.0f, 1.0f);
		switch(game.getState()) {
		case typeChoose:
			game.getTypeChooseScreen().init(pr_matrix);
			break;
		case lobby:
			
			break;
		case onTask:
			
			break;
		}
	}
	
}
