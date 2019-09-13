package com.arjav.gameoflife.server;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Scanner;

import com.arjav.gameoflife.client.game.Type;

public class ServerMain {

	// ranges for public and local multi cast groups are different
	public static final String DISCOVERY_MUTLICAST_GROUP = "239.53.63.243"; // arbitrary
	public static final String REQ_MULTICAST_GROUP = "239.47.52.22"; // arbitrary
	public static final int MULTICAST_SERVER_PORT = 1532;
	public static final int MULTICAST_CLIENT_PORT = 5626;
	public static final int REQ_SEND_CLIENT_PORT = 9186;
	public static final int REQ_RECEIVE_SERVER_PORT = 5145;
	public static final int REQ_RESPONSE_SERVER_PORT = 8913;
	public static final int RESP_RECEIVE_CLIENT_PORT = 2669;
	
	private InetAddress localGroup;
	private InetAddress reqGroup;
	private InetAddress serverAddr;
	
	private DatagramSocket broadcastSocket;
	private DatagramSocket reqResponseSocket;
	private MulticastSocket reqReceiveSocket;
	
	private Thread publicReqProcessThread;
	private Thread privateReqProcessThread;
	private Thread readCommands;
	
	private volatile ArrayList<PlayerRecord> playerList;
	private volatile ArrayList<String[]> registeredUsers;
	
	private boolean running = false;
	
	private ServerMain() {
		try {
			localGroup = InetAddress.getByName(DISCOVERY_MUTLICAST_GROUP);
			reqGroup = InetAddress.getByName(REQ_MULTICAST_GROUP);
			registeredUsers = new ArrayList<String[]>();
			readRegisteredUsers();
			serverAddr = InetAddress.getLocalHost();
			playerList = new ArrayList<PlayerRecord>();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	private void init() {
		try {
			broadcastSocket = new DatagramSocket(MULTICAST_SERVER_PORT);
			reqReceiveSocket = new MulticastSocket(REQ_RECEIVE_SERVER_PORT);
			reqReceiveSocket.joinGroup(reqGroup);
			reqReceiveSocket.setSoTimeout(100);
			reqResponseSocket = new DatagramSocket(REQ_RESPONSE_SERVER_PORT);
			running = true;
			createAndStartReqProcessThread();
			
			privateReqProcessThread = new Thread(new Runnable() {
				@Override
				public void run() {
					while(running) processRequests();
				}
			});
			privateReqProcessThread.start();
			
			readCommands = new Thread(new Runnable() {
				@Override
				public void run() {
					Scanner sc = new Scanner(System.in);
					while(running) {
						String command = sc.nextLine();
						switch(command) {
						case "exit":
							running = false;
							break;
						}
					}
					sc.close();
				}
			});
			readCommands.start();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private synchronized void processRequests() {
		ListIterator<PlayerRecord> iter = playerList.listIterator();
		while(iter.hasNext()){
			PlayerRecord player = iter.next();
			String req = player.getAssociatedClient().peekMessage();
			if(req != null) {
				if(req.equals("GT")) {
					if(player.getType() == null) player.getAssociatedClient().sendMessage("null");
					else player.getAssociatedClient().sendMessage(player.getType().toString());
				}
				else if(req.equals("LO")) {
					iter.remove();
					player.getAssociatedClient().sendMessage("LoggedOut");
				}
				else if(req.startsWith("ST")) {
					String type = req.split(" ")[1];
					player.setType(Type.valueOf(type));
					getRegisteredUserTokens(player.getName())[2] = type;
				}
			}
		}
	}
	
	private synchronized void joinClient(String info) {
		String[] clientInfo = info.split(" ");
		// check if user with username exists in the archives
		// if yes then load player
		// else generate new player
		String[] regUser = userIsRegistered(clientInfo[1]);
		Client c = new Client(clientInfo[0], clientInfo[1], clientInfo[2]);
		PlayerRecord newPlayer;
		if(regUser != null) {
			if(!regUser[1].equals(clientInfo[2])) {
				NetUtils.sendMessage(reqResponseSocket, clientInfo[0] + " IP", reqGroup, RESP_RECEIVE_CLIENT_PORT);
				// incorrect password
				return;
			}
			else {
				newPlayer = new PlayerRecord(0, 0, clientInfo[1], Type.valueOf(regUser[2]), c);
			}
		} else {
			registeredUsers.add(new String[] {clientInfo[1], clientInfo[2], "null"});
			newPlayer = new PlayerRecord(0, 0, clientInfo[1], null, c);
		}
		
		NetUtils.sendMessage(reqResponseSocket, clientInfo[0] + " CP " + c.getServerSocket().getLocalPort(), reqGroup, RESP_RECEIVE_CLIENT_PORT);
		// correct password, here's your dedicated port, client
		
		c.connect();
		String responseReceived = c.getMessage();
		if(responseReceived.equals("initFailure")) {
			c.closeSockets();
			System.out.println("Client " + c.getIPAddr() + " was not able to initialise");
		}
		else {
			playerList.add(newPlayer);
		}
	}
	
	private void createAndStartReqProcessThread() {
		publicReqProcessThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(running) {
					String msgReceived = NetUtils.getMessage(reqReceiveSocket).trim();
					/*
					 * CJ IP -> Client join request
					 * */
					if(msgReceived.startsWith("CJ ")) {
						new Thread(new Runnable() {
							@Override
							public void run() {
								joinClient(msgReceived.substring(3, msgReceived.length()));
							}
						}).start();
						// 
					}
				}
			}
		});
		publicReqProcessThread.start();
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
		running = false;
		try {
			publicReqProcessThread.join();
			privateReqProcessThread.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		broadcastSocket.close();
		try {
			reqReceiveSocket.close();
			reqResponseSocket.close();
			PrintWriter pw = new PrintWriter(new FileWriter(new File(getClass().getResource("/Users.txt").getPath())));
			for(String[] tokens : registeredUsers) {
				String line = "";
				for(String token : tokens) {
					line += token + " ";
				}
				pw.println(line);
			}
			pw.close();
			for(PlayerRecord player : playerList) {
				player.getAssociatedClient().getServerSocket().close();
				player.getAssociatedClient().getSocket().close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	private void readRegisteredUsers() {
		Scanner sc = null;
		sc = new Scanner(getClass().getResourceAsStream("/Users.txt"));
		while(sc.hasNextLine()) {
			registeredUsers.add(sc.nextLine().split(" "));
		}
		sc.close();
	}
	
	public static void main(String[] args) {
		ServerMain sm = new ServerMain();
		sm.init();
		sm.run();
		sm.end();
	}
	
	private String[] userIsRegistered(String username) {
		String[] arr = null;
		for(String[] tokens : registeredUsers) {
			if(tokens[0].equals(username)) {
				arr = tokens;
				break;
			}
		}
		return arr;
	}
	
	private String[] getRegisteredUserTokens(String username) {
		
		for(String[] tokens : registeredUsers) {
			if(tokens[0].equals(username)) return tokens;
		}
		
		return null;
	}
	
}
