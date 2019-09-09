package com.arjav.gameoflife.client.gl;

import org.lwjgl.glfw.GLFW;

public class Window {
	
	private String title;
	private int width, height;
	private long windowHandle;
	
	public Window(String title, int width, int height) {
		this.title = title;
		this.width = width;
		this.height = height;
		windowHandle = GLFW.glfwCreateWindow(width, height, title, 0, 0);
		if(windowHandle == 0) {
			System.err.println("Failed to create window " + title + " (" + width + ", " + height + ")");
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
