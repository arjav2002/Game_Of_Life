package com.arjav.gameoflife.client.game.ui;

import com.arjav.gameoflife.client.game.Game;
import com.arjav.gameoflife.client.game.Type;
import com.arjav.gameoflife.client.game.graphics.Shader;
import com.arjav.gameoflife.maths.Matrix4f;

public class TypeChooseScreen {

	private Button sniperChoose, medicChoose, juggernautChoose;
	private Game game;
	private Shader shader;
	
	public TypeChooseScreen(Game game, String vertexShader, String fragmentShader) {
		this.game = game;
		shader = new Shader(vertexShader, fragmentShader);
		sniperChoose = new Button("/sniper.png", game.getWidth()/7, game.getHeight()/2-game.getHeight()/6, game.getWidth()/7, game.getHeight()/3);
		juggernautChoose = new Button("/juggernaut.png", 3*game.getWidth()/7, game.getHeight()/2-game.getHeight()/6, game.getWidth()/7, game.getHeight()/3);
		medicChoose = new Button("/medic.png", 5*game.getWidth()/7, game.getHeight()/2-game.getHeight()/6, game.getWidth()/7, game.getHeight()/3);
	}
	
	public void render(Matrix4f cameraMatrix) {
		shader.enable();
		shader.setUniformMat4f("camera_matrix", cameraMatrix);

		sniperChoose.render(shader);
		juggernautChoose.render(shader);
		medicChoose.render(shader);
		
		shader.disable();
	}
	
	public void init(Matrix4f prMatrix) {
		shader.setUniformMat4f("pr_matrix", prMatrix);
		sniperChoose.init();
		juggernautChoose.init();
		medicChoose.init();
	}
	
	public void leftMousePress(int mx, int my) {
		if(juggernautChoose.isInBounds(mx, my)) game.setType(Type.juggernaut);
		else if(sniperChoose.isInBounds(mx, my)) game.setType(Type.sniper);
		else if(medicChoose.isInBounds(mx, my)) game.setType(Type.medic);
	}
}
