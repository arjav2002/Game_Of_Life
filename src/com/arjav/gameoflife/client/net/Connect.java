package com.arjav.gameoflife.client.net;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import com.arjav.gameoflife.server.NetUtils;
import com.arjav.gameoflife.server.ServerMain;

public class Connect {
		
	private MulticastSocket discoverySocket;
	private MulticastSocket receiveRespSocket;
	private DatagramSocket reqToServer;
	private Socket dedicatedSocket;
	private String clientIP;
	private InetAddress serverAddr;
	private InetAddress reqGroup;
	
	public void init() {
		try {
			discoverySocket = new MulticastSocket(ServerMain.MULTICAST_CLIENT_PORT);
			discoverySocket.joinGroup(InetAddress.getByName(ServerMain.DISCOVERY_MUTLICAST_GROUP));
			clientIP = InetAddress.getLocalHost().getHostAddress();
			reqGroup = InetAddress.getByName(ServerMain.REQ_MULTICAST_GROUP);
			reqToServer = new DatagramSocket(ServerMain.REQ_SEND_CLIENT_PORT);
			receiveRespSocket = new MulticastSocket(ServerMain.RESP_RECEIVE_CLIENT_PORT);
			receiveRespSocket.joinGroup(reqGroup);
			connectToServer();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Could not initialise multicast sockets or local IPs");
		}
	}
	
	public boolean requestConnection(String username, String password) {
		NetUtils.sendMessage(reqToServer, "CJ " + clientIP + " " + username + " " + password, reqGroup, ServerMain.REQ_RECEIVE_SERVER_PORT);
		String response = "";
		do {
			response = NetUtils.getMessage(receiveRespSocket);
		} while(!response.startsWith(clientIP));
		
		String[] tokens = response.split(" ");
		System.out.println(response);
		if(tokens[1].equals("IP")) return false;
		try {
			dedicatedSocket = new Socket(serverAddr, Integer.parseInt(tokens[2]));
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
		System.out.println(NetUtils.getMessage(dedicatedSocket)); // should print hello from the server
		return true;
	}
	
	public void connectToServer() {
		boolean foundServer = false;
		while(!foundServer) {
			String msg = NetUtils.getMessage(discoverySocket);
			foundServer = msg.startsWith("Gameoflife!");
			if(foundServer) {
				try {
					serverAddr = InetAddress.getByName(msg.split(" ")[1]);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void close() {
		discoverySocket.close();
		reqToServer.close();
	}

}
