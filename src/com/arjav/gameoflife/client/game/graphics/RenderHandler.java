package com.arjav.gameoflife.client.game.graphics;

import com.arjav.gameoflife.client.game.Game;
import com.arjav.gameoflife.client.game.ui.Button;
import com.arjav.gameoflife.maths.Matrix4f;

public class RenderHandler {
	
	private Game game;
	
	public RenderHandler(Game game) {
		this.game = game;
		game.sniperChoose = new Button("shaders/vertex.shd", "shaders/fragment.shd", "/sniper.png", game.getWidth()/7, game.getHeight()/2-game.getHeight()/6, game.getWidth()/7, game.getHeight()/3);
		game.juggernautChoose = new Button("shaders/vertex.shd", "shaders/fragment.shd", "/juggernaut.png", 3*game.getWidth()/7, game.getHeight()/2-game.getHeight()/6, game.getWidth()/7, game.getHeight()/3);
		game.medicChoose = new Button("shaders/vertex.shd", "shaders/fragment.shd", "/medic.png", 5*game.getWidth()/7, game.getHeight()/2-game.getHeight()/6, game.getWidth()/7, game.getHeight()/3);
	}
	
	public void render() {
		
		switch(game.getState()) {
		case typeChoose:
			renderTypeChooseScreen(game.getEventHandler().getMx(), game.getEventHandler().getMy());
			break;
		case lobby:
			break;
		case onTask:
			break;
		default:
			break;
		}
	}
	
	private void renderTypeChooseScreen(int mx, int my) {
		game.sniperChoose.render();
		game.juggernautChoose.render();
		game.medicChoose.render();
	}
	
	public void init() {
		Matrix4f pr_matrix = Matrix4f.orthographic(0, game.getWidth(), game.getWidth() * 9.0f / 16.0f, 0, -1.0f, 1.0f);
		Matrix4f trans_mat = new Matrix4f();
		trans_mat.identity();

		game.sniperChoose.init(pr_matrix, trans_mat);
		game.juggernautChoose.init(pr_matrix, trans_mat);
		game.medicChoose.init(pr_matrix, trans_mat);
		
	}
	
}
