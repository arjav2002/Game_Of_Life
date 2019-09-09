package com.arjav.gameoflife.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class NetUtils {
	
	public static String getMessage(DatagramSocket socket) {
		String msg = "";
		byte[] arr = new byte[1024];
		DatagramPacket pkt = new DatagramPacket(arr, arr.length);
		try {
			socket.receive(pkt);
		} catch (IOException e) {
			e.printStackTrace();
		}
		msg = new String(pkt.getData(), pkt.getOffset(), pkt.getLength());
		return msg;
	}
	
	public static void sendMessage(Socket socket, String str) {
		try {
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			dos.write(str.getBytes());
			dos.close();
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
			String str = null;
			while((str = br.readLine()) != null) msg += str;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return msg;
	}
	
}
