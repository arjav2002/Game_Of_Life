package com.arjav.gameoflife.client.game.graphics;

import org.lwjgl.glfw.GLFW;

public class Window {
	
	private String title;
	private int width, height;
	private long windowHandle;
	
	public Window(String title, int width, int height) throws WindowNotCreatedException {
		this.title = title;
		this.width = width;
		this.height = height;
		windowHandle = GLFW.glfwCreateWindow(width, height, title, 0, 0);
		if(windowHandle == 0) {
			throw new WindowNotCreatedException("Could not create window " + title + " " + width + "x" + height);
		}
	}
	
	public String getTitle() {
		return title;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public long getWindowHandle() {
		return windowHandle;
	}
	
	public void destroy() {
		GLFW.glfwDestroyWindow(windowHandle);
	}

}
