package com.arjav.gameoflife.server;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ServerMain {

	// ranges for public and local multi cast groups are different
	public static final String DISCOVERY_MUTLICAST_GROUP = "239.53.63.243"; // arbitrary
	public static final String REQ_MULTICAST_GROUP = "239.47.52.22"; // arbitrary
	public static final int MULTICAST_SERVER_PORT = 2234;
	public static final int MULTICAST_CLIENT_PORT = 5572;
	public static final int REQ_SEND_CLIENT_PORT = 4852;
	public static final int REQ_RECEIVE_SERVER_PORT = 9382;
	public static final int REQ_RESPONSE_SERVER_PORT = 5829;
	public static final int RESP_RECEIVE_CLIENT_PORT = 6289;
	
	private InetAddress localGroup;
	private InetAddress reqGroup;
	private InetAddress serverAddr;
	
	private DatagramSocket broadcastSocket;
	private DatagramSocket reqResponseSocket;
	private MulticastSocket reqReceiveSocket;
	
	private Thread reqProcess;
	
	private ArrayList<Client> clientList;
	private Map<String, String> registeredUsers;
	
	private volatile boolean running = false;
	
	private ServerMain() {
		try {
			localGroup = InetAddress.getByName(DISCOVERY_MUTLICAST_GROUP);
			reqGroup = InetAddress.getByName(REQ_MULTICAST_GROUP);
			clientList = new ArrayList<Client>();
			registeredUsers = new HashMap<String, String>();
			readRegisteredUsers();
			serverAddr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	private void init() {
		try {
			broadcastSocket = new DatagramSocket(MULTICAST_SERVER_PORT);
			reqReceiveSocket = new MulticastSocket(REQ_RECEIVE_SERVER_PORT);
			reqReceiveSocket.joinGroup(reqGroup);
			reqResponseSocket = new DatagramSocket(REQ_RESPONSE_SERVER_PORT);
			running = true;
			createAndStartReqProcessThread();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void joinClient(String info) {
		String[] clientInfo = info.split(" ");
		// check if user with username exists in the archives
		// if yes then load player
		// else generate new player
		if(registeredUsers.containsKey(clientInfo[1])) {
			if(!registeredUsers.get(clientInfo[1]).equals(clientInfo[2])) {
				NetUtils.sendMessage(reqResponseSocket, clientInfo[0] + " IP", reqGroup, RESP_RECEIVE_CLIENT_PORT); // incorrect password
				return;
			}
		} else registeredUsers.put(clientInfo[1], clientInfo[2]);
		
		Client c = new Client(clientInfo[0], clientInfo[1], clientInfo[2]);
		clientList.add(c);
		NetUtils.sendMessage(reqResponseSocket, clientInfo[0] + " CP " + c.getServerSocket().getLocalPort(), reqGroup, RESP_RECEIVE_CLIENT_PORT); // correct password, here's your dedicated port
		c.connect();
		System.out.println(clientInfo[1] + " " + clientInfo[2] + " ADDED!");
		NetUtils.sendMessage(c.getSocket(), "Hello from the server!");
	}
	
	private void createAndStartReqProcessThread() {
		reqProcess = new Thread(new Runnable() {
			@Override
			public void run() {
				while(running) {
					String msgReceived = NetUtils.getMessage(reqReceiveSocket).trim();
					/*
					 * CJ IP -> Client join request
					 * */
					if(msgReceived.startsWith("CJ ")) {
						joinClient(msgReceived.substring(3, msgReceived.length()));
					}
				}
			}
		});
		reqProcess.start();
	}
	
	private void run() {
		while(running) {
			broadcastLocal("Gameoflife! " + serverAddr.getHostAddress());
		}
	}
	
	private void broadcast(byte[] msg, DatagramSocket socket, InetAddress group, int port) {
		try {
			DatagramPacket packet = new DatagramPacket(msg, msg.length, group, port);
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void broadcastLocal(String message) {
		byte[] msg = message.getBytes();
		broadcast(msg, broadcastSocket, localGroup, MULTICAST_CLIENT_PORT);
	}
	
	private void end() {
		broadcastSocket.close();
		try {
			reqReceiveSocket.close();
			reqResponseSocket.close();
			PrintWriter pw = new PrintWriter(new FileWriter(new File("/Users.txt")));
			for(Map.Entry<String, String> entry : registeredUsers.entrySet()) {
				pw.println(entry.getKey() + " " + entry.getValue());
			}
			pw.close();
			for(Client c : clientList) {
				c.getServerSocket().close();
				c.getSocket().close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void readRegisteredUsers() {
		Scanner sc = null;
		sc = new Scanner(getClass().getResourceAsStream("/Users.txt"));
		while(sc.hasNextLine()) {
			String[] tokens = sc.nextLine().split(" ");
			registeredUsers.put(tokens[0], tokens[1]);
		}
		sc.close();
	}
	
	public static void main(String[] args) {
		ServerMain sm = new ServerMain();
		sm.init();
		sm.run();
		sm.end();
	}
	
}
