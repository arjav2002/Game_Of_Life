package com.arjav.gameoflife.client.game;

import java.awt.Rectangle;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.opengl.GL11.*;

import com.arjav.gameoflife.client.FrameTimer;
import com.arjav.gameoflife.client.game.graphics.RenderHandler;
import com.arjav.gameoflife.client.game.graphics.Window;
import com.arjav.gameoflife.client.game.graphics.WindowNotCreatedException;
import com.arjav.gameoflife.client.net.Connect;

public class Game implements Runnable {
	
	private Window window;
	private Connect connect;
	private State st;
	private Type soldierType;
	private GLFWVidMode videoMode;
	private Thread myThread;
	private RenderHandler renderHandler;
	private EventHandler eventHandler;
	
	private static final int FPS = 60;
	private int frames;
	private Rectangle chooseMedicRect, chooseJuggernautRect, chooseSniperRect;
	
	public Game(String title, int width, int height, Connect connect) throws WindowNotCreatedException {
		this.window = new Window(title, width, height);
		videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		GLFW.glfwSetWindowPos(window.getWindowHandle(), (videoMode.width()-width)/2, (videoMode.height()-height)/2);
		
		this.connect = connect;
		myThread = new Thread(this);
		renderHandler = new RenderHandler(this);
		eventHandler = new EventHandler(this);
		connect.sendMessage("initSuccess");
		
		chooseMedicRect = new Rectangle((window.getWidth()-window.getWidth()/5)/3, window.getHeight()/4, window.getWidth()/5, window.getHeight()/2);
		chooseJuggernautRect = new Rectangle(2*(window.getWidth()-window.getWidth()/5)/3 + window.getWidth()/5, window.getHeight()/4, window.getWidth()/5, window.getHeight()/2);
		chooseSniperRect = new Rectangle((window.getWidth()-window.getWidth()/5) + 2*window.getWidth()/5, window.getHeight()/4, window.getWidth()/5, window.getHeight()/2);
	}
	
	// some error happens during initialisation, send it through connect
	// else send confirmation
	
	public void init() {
		// ascertain type of soldier and gamestate
		connect.sendMessage("GT");
		String type = connect.getMessage();
		if(type.equals("null")) {
			st = State.typeChoose;
			soldierType = null;
		}
		else {
			st = State.lobby;
			if(type.equals("juggernaut")) soldierType = Type.juggernaut;
			else if(type.equals("sniper")) soldierType = Type.sniper;
			else if(type.equals("medic")) soldierType = Type.medic;
		}
	}
	
	@Override
	public void run() {
		GLFW.glfwMakeContextCurrent(window.getWindowHandle());
		GLFW.glfwShowWindow(window.getWindowHandle());
		createCapabilities();
		System.out.println("OpenGL: " + glGetString(GL_VERSION));
		
		FrameTimer ft = new FrameTimer(System.nanoTime());
		double delta = 1.0/FPS * Math.pow(10, 9);
		long timer = System.currentTimeMillis();
		
		while(!GLFW.glfwWindowShouldClose(window.getWindowHandle())) {
			GLFW.glfwPollEvents();
			
			eventHandler.handleEvents();
			
			if(ft.mark() >= delta) {
				tick();
			}
			
			renderHandler.render();
			frames++;
			
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: " + frames);
				timer = System.currentTimeMillis();
				frames = 0;
			}
		}
		stop();
	}
	
	public void start() {
		myThread.start();
	}
	
	public void stop() {
		GLFW.glfwDestroyWindow(window.getWindowHandle());
		GLFW.glfwTerminate();
		System.exit(0);
	}
	
	private void tick() {
		
	}

	public State getState() {
		return st;
	}
	
	public EventHandler getEventHandler() {
		return eventHandler;
	}
	
	public long getWindowHandle() {
		return window.getWindowHandle();
	}
	
}
