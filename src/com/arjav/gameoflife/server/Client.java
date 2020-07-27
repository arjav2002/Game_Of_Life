package com.arjav.gameoflife.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.arjav.gameoflife.client.glutils.FileUtils;

public class Client {
	
	private String IPaddr; // client's IP address
	private ServerSocket serverSock; // dedicated socket for the client
	private Socket socket;
	private User user;
	private BufferedReader br;
	private PrintWriter pw;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	public Client(String IPaddr, String username, String password) {
		this.IPaddr = IPaddr;
		user = new User(username, password);
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
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String msg) {
		pw.println(msg);
		pw.flush();
	}
	
	public void sendObject(Object obj) {
		try {
			String msg = getMessage();
			while(!msg.equals("SEND")) msg = getMessage();
			oos.writeObject(obj);
			oos.flush();
		} catch (IOException e) {
			System.err.println("Not able to send object to server");
			e.printStackTrace();
		}
	}
	
	public Object getObject() {
		Object obj = null;
		try {
			sendMessage("SEND");
			while(obj == null) {
				obj = ois.readObject();			
			}
		} catch (ClassNotFoundException | IOException e) {
			System.err.println("Not able to read objects from server");
			e.printStackTrace();
		}
		return obj;
	}
	
	public String peekMessage() {
		String msg = "";
		try {
			msg = br.readLine();
		} catch (IOException e) {
			System.err.println("Not able to peek message for client: " + IPaddr);
			e.printStackTrace();
		}
		return msg;
	}
	
	public String getMessage() {
		String msg = "";
		try {
			while(FileUtils.isEmpty(msg)) {
				msg = br.readLine();
			}
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
	
	public User getUser() {
		return user;
	}

}
