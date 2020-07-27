package com.arjav.gameoflife.server;

import com.arjav.gameoflife.client.game.Type;

public class User {

	private String username;
	private String password;
	private Type type;
	
	public User(String username, String password, String type) {
		this(username, password);
		this.type = Type.valueOf(type);
	}
	
	public User(String username, String password, Type type) {
		this(username, password);
		this.type = type;
	}
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
		this.type = null;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public Type getType() {
		return type;
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	public void setType(String type) {
		this.type = Type.valueOf(type);
	}
	
}
