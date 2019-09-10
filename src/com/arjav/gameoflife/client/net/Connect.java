package com.arjav.gameoflife.client.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;

import com.arjav.gameoflife.server.NetUtils;
import com.arjav.gameoflife.server.ServerMain;

public class Connect {
		
	private MulticastSocket discoverySocket;
	private MulticastSocket receiveRespSocket;
	private DatagramSocket reqToServer;
	private Socket dedicatedSocket;
	private String clientIP;
	private InetAddress reqGroup;
	private BufferedReader br;
	private PrintWriter pw;
	private volatile boolean searchForServers = false;
	
	public void init() {
		try {
			discoverySocket = new MulticastSocket(ServerMain.MULTICAST_CLIENT_PORT);
			discoverySocket.joinGroup(InetAddress.getByName(ServerMain.DISCOVERY_MUTLICAST_GROUP));
			clientIP = InetAddress.getLocalHost().getHostAddress();
			reqGroup = InetAddress.getByName(ServerMain.REQ_MULTICAST_GROUP);
			reqToServer = new DatagramSocket(ServerMain.REQ_SEND_CLIENT_PORT);
			receiveRespSocket = new MulticastSocket(ServerMain.RESP_RECEIVE_CLIENT_PORT);
			receiveRespSocket.joinGroup(reqGroup);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Could not initialise multicast sockets or local IPs");
		}
	}
	
	public boolean requestConnection(String username, String password, String serverAddr) {
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
			br = new BufferedReader(new InputStreamReader(dedicatedSocket.getInputStream()));
			pw = new PrintWriter(dedicatedSocket.getOutputStream());
		} catch (NumberFormatException | IOException e) {
			System.err.println("not able to initialise sockets");
			e.printStackTrace();
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
		reqToServer.close();
		try {
			dedicatedSocket.close();
			br.close();
			pw.close();
		} catch (IOException e) {
			System.err.println("Not able to close dedicated socket");
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String str) {
		pw.println(str);
		pw.flush();
	}
	
	public String getMessage() {
		String msg = "";
		try {
			// important distinction from the server getMessage code
			while(msg.equals("")) msg = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return msg;
	}
	
	public void searchForServers(boolean searchForServers) {
		this.searchForServers = searchForServers;
	}

}
