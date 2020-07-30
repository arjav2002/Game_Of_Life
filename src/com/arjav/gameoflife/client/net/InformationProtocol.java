package com.arjav.gameoflife.client.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import com.arjav.gameoflife.client.glutils.FileUtils;

public class InformationProtocol {

	protected BufferedReader br;
	protected PrintWriter pw;
	protected ObjectInputStream ois;
	protected ObjectOutputStream oos;
	
	public void initStreams(Socket socket) throws IOException {
		br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		pw = new PrintWriter(socket.getOutputStream());
		oos = new ObjectOutputStream(socket.getOutputStream());
		ois = new ObjectInputStream(socket.getInputStream());
	}
	
	public synchronized void sendMessage(String str) {
		pw.println(str + "\n");
		pw.flush();
	}
	
	public synchronized String getMessage() {
		String msg = "";
		try {
			while(FileUtils.isEmpty(msg)) {
				msg = br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return msg;
	}
	
	public synchronized void sendObject(Object obj) {
		try {
			String msg;
			do {
				msg = getMessage();
			} while(!msg.equals("SEND"));
			if(obj == null) obj = "null";
			oos.writeObject(obj);
			oos.flush();
		} catch (IOException e) {
			System.err.println("Not able to send object to server");
			e.printStackTrace();
		}
	}
	
	public synchronized Object getObject() {
		Object obj = null;
		try {
			sendMessage("SEND");
			while(obj == null) {
				obj = ois.readObject();
				if(obj instanceof String && ((String)obj).equals("null")) return null;
			}
		} catch (ClassNotFoundException | IOException e) {
			System.err.println("Not able to read objects from server");
			e.printStackTrace();
		}
		return obj;
	}
	
}
