package com.arjav.gameoflife.client.game;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class EventHandler {

	private int mx, my;
	private boolean[] keys;
	private static final int N_KEYS = 512;
	private Game game;
	
	public EventHandler(Game game) {
		keys = new boolean[N_KEYS];
		for(int i = 0; i < N_KEYS; i++) keys[i] = false;
		this.game = game;
		GLFW.glfwSetCursorPosCallback(game.getWindowHandle(), new GLFWCursorPosCallback() {
			@Override
			public void invoke(long window, double xpos, double ypos) {
				cursorMovedCallback(xpos, ypos);
			}
		});
		GLFW.glfwSetMouseButtonCallback(game.getWindowHandle(), new GLFWMouseButtonCallback() {
			@Override
			public void invoke(long window, int button, int action, int mods) {
				if(button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
					if(action == GLFW.GLFW_PRESS) onLeftMousePress();
					else if(action == GLFW.GLFW_RELEASE) onLeftMouseRelease();
				}
			}
			
		});
	}
	
	public void cursorMovedCallback(double xpos, double ypos) {
		mx = (int)xpos;
		my = (int)ypos;
	}
	
	public void onLeftMousePress() {
	}
	
	public void onLeftMouseRelease() {
		
	}
	
	public void handleEvents() {
	}

	public int getMx() {
		return mx;
	}
	
	public int getMy() {
		return my;
	}
	
}