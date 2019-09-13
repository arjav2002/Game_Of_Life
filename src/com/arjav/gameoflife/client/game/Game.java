package com.arjav.gameoflife.client.game;

import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

import com.arjav.gameoflife.client.FrameTimer;
import com.arjav.gameoflife.client.game.entities.Player;
import com.arjav.gameoflife.client.game.graphics.RenderHandler;
import com.arjav.gameoflife.client.game.graphics.Window;
import com.arjav.gameoflife.client.game.graphics.WindowNotCreatedException;
import com.arjav.gameoflife.client.game.ui.TypeChooseScreen;
import com.arjav.gameoflife.client.net.Connect;

public class Game implements Runnable {
	
	private Window window;
	private Connect connect;
	private State st;
	private Player player;
	private GLFWVidMode videoMode;
	private Thread myThread;
	private RenderHandler renderHandler;
	private EventHandler eventHandler;
	private String title;
	private int width, height;
	private TypeChooseScreen typeChooseScreen;
	private Camera camera;
	
	private static final int FPS = 60;
	
	public Game(String title, int width, int height, Connect connect, String username) throws WindowNotCreatedException {		
		this.connect = connect;
		this.title = title;
		this.width = width;
		this.height = height;
		myThread = new Thread(this);
		camera = new Camera(null);
		player = new Player("", 0, 0, username, null);
	}
	
	
	private void init() {
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
		try {
			window = new Window(title, width, height);
		} catch (WindowNotCreatedException e) {
			System.err.println("Could not create window");
			connect.sendMessage("initFailure");
			stop();
			e.printStackTrace();
		}
		videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		GLFW.glfwMakeContextCurrent(window.getWindowHandle());
		createCapabilities();
		connect.sendMessage("initSuccess");
		
		// ascertain type of soldier and gamestate
		connect.sendMessage("GT");
		String type = connect.getMessage();
		if(type.equals("null")) {
			st = State.typeChoose;
			player.setType(null);
			typeChooseScreen = new TypeChooseScreen(this, "/vertex.shd", "/fragment.shd");
		}
		else {
			st = State.lobby;
			player.setType(Type.valueOf(type));
		}
		
		GLFW.glfwSetWindowPos(window.getWindowHandle(), (videoMode.width()-width)/2, (videoMode.height()-height)/2);
		renderHandler = new RenderHandler(this);
		eventHandler = new EventHandler(this);
		renderHandler.init();
		
		GLFW.glfwShowWindow(window.getWindowHandle());

	}
	
	@Override
	public void run() {
		init();
		
		FrameTimer ft = new FrameTimer(System.nanoTime());
		final double delta = 1.0/FPS * Math.pow(10, 9);
		double elapsed = 0.0;
		long timer = System.currentTimeMillis();
		int frames = 0, ticks = 0;
		
		while(!GLFW.glfwWindowShouldClose(window.getWindowHandle())) {
			GLFW.glfwPollEvents();
			
			eventHandler.handleEvents();
			
			elapsed += ft.mark();
			
			if(elapsed >= delta) {
				tick();
				elapsed -= delta;
				ticks++;
			}
			
			glClear(GL_COLOR_BUFFER_BIT);
			renderHandler.render();
			frames++;
			
			GLFW.glfwSwapBuffers(window.getWindowHandle());
			
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: " + frames + ", ticks: " + ticks);
				timer = System.currentTimeMillis();
				frames = 0;
				ticks = 0;
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
		connect.sendMessage("LO");
		System.out.println(connect.getMessage()); // wait for server to send Logged out message
		System.exit(0);
	}
	
	public void setType(Type ty) {
		connect.sendMessage("ST " + ty.toString());
		player.setType(ty);
		st = State.lobby;
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
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public TypeChooseScreen getTypeChooseScreen() {
		return typeChooseScreen;
	}
	
	public Camera getCamera() {
		return camera;
	}
}
