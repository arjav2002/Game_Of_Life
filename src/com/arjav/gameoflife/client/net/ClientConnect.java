package com.arjav.gameoflife.client.net;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;

import com.arjav.gameoflife.server.NetUtils;
import com.arjav.gameoflife.server.ServerMain;

public class ClientConnect extends InformationProtocol {
		
	private MulticastSocket discoverySocket;
	private Socket reqServerSocket;
	private Socket dedicatedSocket;
	private String clientIP;
	
	private volatile boolean searchForServers = false;
	
	public void init() {
		try {
			discoverySocket = new MulticastSocket(ServerMain.MULTICAST_CLIENT_PORT);
			discoverySocket.joinGroup(InetAddress.getByName(ServerMain.DISCOVERY_MUTLICAST_GROUP));
			clientIP = getLocalIPAddress();
			
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Could not initialise multicast sockets or local IPs");
		}
	}
	
	public static String getLocalIPAddress() {
		String ip = "";
		DatagramSocket socket;
		try {
			socket = new DatagramSocket();
			socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
			ip = socket.getLocalAddress().getHostAddress();
		} catch (SocketException | UnknownHostException e) {
			e.printStackTrace();
		}
		
		return ip;
	}
	
	public boolean requestConnection(String username, String password, String serverAddr) {
		String response = "";
		try {
			reqServerSocket = new Socket(serverAddr, ServerMain.REQ_PORT);
			System.out.println("Getting OK");
			NetUtils.getMessage(reqServerSocket);
			System.out.println("Gotten OK, Requesting connect");
			NetUtils.sendMessage(reqServerSocket, "CJ " + clientIP + " " + username + " " + password);
			System.out.println("Waiting on response");
			response = NetUtils.getMessage(reqServerSocket);
			NetUtils.sendMessage(reqServerSocket, "OK");
			String[] tokens = response.split(" ");
			System.out.println(response);
			if(!tokens[1].equals("CP")) return false;
			try {
				dedicatedSocket = new Socket(serverAddr, Integer.parseInt(tokens[2]));
				initStreams(dedicatedSocket);
			} catch (NumberFormatException | IOException e) {
				System.err.println("not able to initialise sockets");
				e.printStackTrace();
			}
			try {
				reqServerSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return true;
	}
	
	public void findServers(ArrayList<String> serverList, DefaultComboBoxModel<String> comboBoxModel) {
		while(searchForServers) {
			String msg = NetUtils.getMessage(discoverySocket);
			if(msg.startsWith("Gameoflife!")) {
				String str = msg.split(" ")[1];
				boolean alreadyExists = false;
				for(String server : serverList) {
					if(server.equals(str)) {
						alreadyExists = true;
						break;
					}
				}
				if(!alreadyExists) {
					serverList.add(str);
					comboBoxModel.addElement(str);
				}
			}
		}
	}
	
	public void close() {
		discoverySocket.close();
		try {
			dedicatedSocket.close();
			br.close();
			pw.close();
		} catch (IOException e) {
			System.err.println("Not able to close dedicated socket");
			e.printStackTrace();
		}
	}
	
	
	
	public void searchForServers(boolean searchForServers) {
		this.searchForServers = searchForServers;
	}

}
