package com.arjav.gameoflife.client.game;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class EventHandler {

	private int mx, my;
	private boolean[] keys;
	private static final int N_KEYS = 512;
	private Game game;
	int x = 0;
	
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
		GLFW.glfwSetKeyCallback(game.getWindowHandle(), new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if(game.getState() != State.typeChoose) {
					switch(key) {
					case GLFW.GLFW_KEY_D:
						if(action == GLFW.GLFW_PRESS) {
							game.getLobby().getPlayer().setVelX(5.0f);
						}
						else if(action == GLFW.GLFW_RELEASE) {
							game.getLobby().getPlayer().setVelX(0.0f);
						}
					break;
					case GLFW.GLFW_KEY_A:
						if(action == GLFW.GLFW_PRESS) {
							game.getLobby().getPlayer().setVelX(-5.0f);
						}
						else if(action == GLFW.GLFW_RELEASE) {
							game.getLobby().getPlayer().setVelX(0.0f);
						}
					break;
					case GLFW.GLFW_KEY_W:
						if(action == GLFW.GLFW_PRESS) {
							game.getLobby().getPlayer().setVelY(-30.0f);
						}
					break;
					}
				}
			}
		});
	}
	
	public void cursorMovedCallback(double xpos, double ypos) {
		mx = (int)xpos;
		my = (int)ypos;
	}
	
	public void onLeftMousePress() {
		switch(game.getState()) {
		case typeChoose:
			game.getTypeChooseScreen().leftMousePress(mx, my);
			break;
		case lobby:
			
			break;
		case onTask:
			
			break;
		}
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