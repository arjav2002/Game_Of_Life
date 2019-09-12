package com.arjav.gameoflife.client.game.graphics;

import org.lwjgl.glfw.GLFW;

import com.arjav.gameoflife.client.game.Game;
import com.arjav.gameoflife.maths.Matrix4f;

import static org.lwjgl.opengl.GL11.*;

public class RenderHandler {
	
	private Game game;
	private Model testModel;
	
	public RenderHandler(Game game) {
		this.game = game;
		testModel = new Model("shaders/vertex.shd", "shaders/fragment.shd", "/swag.png", new float[] {
				-1.0f, 1.0f, 1.0f,
				1.0f, 1.0f, 1.0f,
				1.0f, -1.0f, 1.0f,
				-1.0f, -1.0f, 1.0f
		}, new byte[] {
				3, 1, 0,
				3, 2, 1
		}, new float[] {
				0.0f, 1.0f,
				1.0f, 1.0f,
				1.0f, 0.0f,
				0.0f, 0.0f
		});
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
		testModel.render();
	}
	
	public void init() {
		testModel.init();
		Matrix4f pr_matrix = Matrix4f.orthographic(-10.0f, 10.0f, -10.0f * 9.0f / 16.0f, 10.0f * 9.0f / 16.0f, -1.0f, 1.0f);
		testModel.getShader().setUniformMat4f("pr_matrix", pr_matrix);
	}
	
}
