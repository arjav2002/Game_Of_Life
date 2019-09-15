package com.arjav.gameoflife.client.game.ui;

import com.arjav.gameoflife.client.game.Camera;
import com.arjav.gameoflife.client.game.Game;
import com.arjav.gameoflife.client.game.Type;
import com.arjav.gameoflife.maths.Matrix4f;

public class TypeChooseScreen extends GameState {

	private Button sniperChoose, medicChoose, juggernautChoose;

	public TypeChooseScreen(Game game, String vertexShader, String fragmentShader) {
		super(game, vertexShader, fragmentShader);
		sniperChoose = new Button("/sniper.png", game.getWidth()/7, game.getHeight()/2-game.getHeight()/6, game.getWidth()/7, game.getHeight()/3);
		juggernautChoose = new Button("/juggernaut.png", 3*game.getWidth()/7, game.getHeight()/2-game.getHeight()/6, game.getWidth()/7, game.getHeight()/3);
		medicChoose = new Button("/medic.png", 5*game.getWidth()/7, game.getHeight()/2-game.getHeight()/6, game.getWidth()/7, game.getHeight()/3);
	}
	
	@Override
	public void render(Camera camera) {
		shader.enable();
		shader.setUniformMat4f("camera_matrix", camera.getViewMatrix());

		sniperChoose.render(shader, camera);
		juggernautChoose.render(shader, camera);
		medicChoose.render(shader, camera);
		
		shader.disable();
	}
	
	@Override
	public void init(Matrix4f prMatrix) {
		super.init(prMatrix);
		sniperChoose.init();
		juggernautChoose.init();
		medicChoose.init();
	}
	
	public void leftMousePress(int mx, int my) {
		if(juggernautChoose.isInBounds(mx, my)) game.setType(Type.juggernaut);
		else if(sniperChoose.isInBounds(mx, my)) game.setType(Type.sniper);
		else if(medicChoose.isInBounds(mx, my)) game.setType(Type.medic);
	}
	
	@Override
	public void tick() {
		// not using
	}
}
