package com.arjav.gameoflife.client.game;

import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

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
	private String title;
	private int width, height;
	
	private static final int FPS = 60;
	private int frames;
	
	public Game(String title, int width, int height, Connect connect) throws WindowNotCreatedException {		
		this.connect = connect;
		this.title = title;
		this.width = width;
		this.height = height;
		myThread = new Thread(this);
		

		//chooseMedicRect = new Rectangle((window.getWidth()-window.getWidth()/5)/3, window.getHeight()/4, window.getWidth()/5, window.getHeight()/2);
		//chooseJuggernautRect = new Rectangle(2*(window.getWidth()-window.getWidth()/5)/3 + window.getWidth()/5, window.getHeight()/4, window.getWidth()/5, window.getHeight()/2);
		//hooseSniperRect = new Rectangle((window.getWidth()-window.getWidth()/5) + 2*window.getWidth()/5, window.getHeight()/4, window.getWidth()/5, window.getHeight()/2);
	}
	
	// some error happens during initialisation, send it through connect
	// else send confirmation
	
	/*private void init() {
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);

		try {
			window = new Window(title, width, height);
		} catch (WindowNotCreatedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		
		GLFW.glfwMakeContextCurrent(window.getWindowHandle());
		GLFW.glfwShowWindow(window.getWindowHandle());
		createCapabilities();
		System.out.println("OpenGL: " + glGetString(GL_VERSION));
		glEnable(GL_DEPTH_TEST);
		renderHandler.init();
		
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

	}*/
	
	private void init() {
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		try {
			window = new Window(title, width, height);
		} catch (WindowNotCreatedException e) {
			System.err.println("Could not create window");
			e.printStackTrace();
		}
		videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		GLFW.glfwMakeContextCurrent(window.getWindowHandle());
		createCapabilities();
		
		GLFW.glfwSetWindowPos(window.getWindowHandle(), (videoMode.width()-width)/2, (videoMode.height()-height)/2);
		renderHandler = new RenderHandler(this);
		eventHandler = new EventHandler(this);
		renderHandler.init();
		connect.sendMessage("initSuccess");
		
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
		
		GLFW.glfwShowWindow(window.getWindowHandle());

	}
	
	@Override
	public void run() {
		init();
		
		while(!GLFW.glfwWindowShouldClose(window.getWindowHandle())) {
			GLFW.glfwPollEvents();
			
			glClear(GL_COLOR_BUFFER_BIT);
			
			renderHandler.render();
			
			GLFW.glfwSwapBuffers(window.getWindowHandle());
		}
		
		GLFW.glfwTerminate();
		/*init();
		
		FrameTimer ft = new FrameTimer(System.nanoTime());
		double delta = 1.0/FPS * Math.pow(10, 9);
		long timer = System.currentTimeMillis();
		
		while(!GLFW.glfwWindowShouldClose(window.getWindowHandle())) {
			GLFW.glfwPollEvents();
			
			//eventHandler.handleEvents();
			
			if(ft.mark() >= delta) {
				tick();
			}
			
			glClear(GL_COLOR_BUFFER_BIT);
			
			glBegin(GL_QUADS);
			glVertex2f(-0.5f, 0.5f);
			glVertex2f(0.5f, 0.5f);
			glVertex2f(0.5f, -0.5f);
			glVertex2f(-0.5f, -0.5f);
			glEnd();
			
			GLFW.glfwSwapBuffers(getWindowHandle());
			
		//	renderHandler.render();
			frames++;
			
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: " + frames);
				timer = System.currentTimeMillis();
				frames = 0;
			}
		}*/
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
