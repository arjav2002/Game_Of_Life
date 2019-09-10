package com.arjav.gameoflife.client.game;

import com.arjav.gameoflife.client.gl.Window;
import com.arjav.gameoflife.client.gl.WindowNotCreatedException;
import com.arjav.gameoflife.client.net.Connect;

public class Game {
	
	private Window window;
	private Connect connect;
	private State st;
	private Type soldierType;
	
	public Game(String title, int width, int height, Connect connect) throws WindowNotCreatedException {
		this.window = new Window(title, width, height);
		this.connect = connect;
		connect.sendMessage("initSuccess");
	}
	
	// some error happens during initialisation, send it through connect
	// else send confirmation
	
	public void init() {
		// ascertain type of soldier and gamestate
		connect.sendMessage("GT");
		String type = connect.getMessage();
		if(type.equals("null")) {
			st = State.characterChoose;
			soldierType = null;
		}
		else {
			st = State.lobby;
			if(type.equals("juggernaut")) soldierType = Type.juggernaut;
			else if(type.equals("sniper")) soldierType = Type.sniper;
			else if(type.equals("medic")) soldierType = Type.medic;
		}
	}

}
