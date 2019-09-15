package com.arjav.gameoflife.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

import com.arjav.gameoflife.client.glutils.FileUtils;

public class NetUtils {
	
	public static String getMessage(DatagramSocket socket) {
		String msg = "";
		byte[] arr = new byte[1024];
		DatagramPacket pkt = new DatagramPacket(arr, arr.length);
		try {
			socket.receive(pkt);
		} catch (IOException e) {
		}
		msg = new String(pkt.getData(), pkt.getOffset(), pkt.getLength());
		return msg;
	}
	
	public static void sendMessage(Socket socket, String str) {
		try {
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			PrintWriter pw = new PrintWriter(dos);
			pw.println(str);
			pw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void sendMessage(DatagramSocket socket, String str, InetAddress grp, int port) {
		byte[] msg = str.getBytes();
		DatagramPacket pkt = new DatagramPacket(msg, msg.length, grp, port);
		try {
			socket.send(pkt);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getMessage(Socket socket) {
		String msg = "";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			msg = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return msg;
	}
	
	public static Object getObject(Socket socket) {
		Object obj = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			obj = ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Could not read Object from " + socket.getInetAddress().toString());
			e.printStackTrace();
		}
		return obj;
	}
	
	public static void sendObject(Socket socket, Object obj) {
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(obj);
			oos.flush();
		} catch (IOException e) {
			System.out.println("Not able to send object: " + obj);
			e.printStackTrace();
		}
	}
}
