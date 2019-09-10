package com.arjav.gameoflife.client.game;

import org.lwjgl.glfw.GLFW;
import static org.lwjgl.opengl.GL11.*;

public class RenderHandler {
	
	private Game game;
	
	public RenderHandler(Game game) {
		this.game = game;
	}
	
	public void render() {
		glClear(GL_COLOR_BUFFER_BIT);
		
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
		
		GLFW.glfwSwapBuffers(game.getWindowHandle());
	}
	
	public void renderTypeChooseScreen(int mx, int my) {
	}
	
}
