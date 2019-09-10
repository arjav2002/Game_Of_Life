package com.arjav.gameoflife.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {
	
	private String IPaddr; // client's IP address
	private ServerSocket serverSock; // dedicated socket for the client
	private Socket socket;
	private String username;
	private String password;
	private BufferedReader br;
	private PrintWriter pw;
	
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
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String msg) {
		pw.println(msg);
		pw.flush();
	}
	
	public String getMessage() {
		String msg = "";
		try {
			msg = br.readLine();
		} catch (IOException e) {
			System.err.println("Not able to get message for client: " + IPaddr);
			e.printStackTrace();
		}
		return msg;
	}
	
	public void closeSockets() {
		try {
			serverSock.close();
			socket.close();
		} catch (IOException e) {
			System.err.println("Not able to close sockets for client: " + IPaddr);
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
