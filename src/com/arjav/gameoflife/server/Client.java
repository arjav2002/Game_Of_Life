package com.arjav.gameoflife.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {
	
	private String IPaddr; // client's IP address
	private ServerSocket serverSock; // dedicated socket for the client
	private Socket socket;
	private String username;
	private String password;
	
	public Client(String IPaddr, String username, String password) {
		this.IPaddr = IPaddr;
		this.username = username;
		this.password = password;
		try {
			serverSock = new ServerSocket(0);
		} catch (IOException e) {
			System.err.println("Was not able to initialise socket for client " + IPaddr);
			e.printStackTrace();
		}
	}
	
	public void connect() {
		try {
			socket = serverSock.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getIPAddr() {
		return IPaddr;
	}
	
	public ServerSocket getServerSocket() {
		return serverSock;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}

}
