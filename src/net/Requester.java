package net;
import java.io.IOException;
import java.net.*;
import java.util.*;

import common.*;

public class Requester{

	private final int BUFFER_SIZE = 1024;
	
	private DatagramSocket socket;
	
 	public Requester() {
 		this(-1);
	}
	
	public Requester(int port) {
		try {
			if (port < 0) {
				this.socket = new DatagramSocket();
			} else {
				this.socket = new DatagramSocket(port);
			}
		} catch (SocketException se) {   // Can't create the socket.
			se.printStackTrace();
			System.exit(1);
		}
	}
	
	public Message sendRequest(InetAddress addr, int port, Message msg) throws PacketException {
		
		DatagramPacket sendPacket;
		byte[] bytes = this.buildMessageBytes(msg);
		
		try {
			sendPacket = new DatagramPacket(bytes, bytes.length, InetAddress.getLocalHost(), port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			throw new PacketException("Failed to create new DatagramPacket");
		}
		
		try {
			socket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return waitForResponse();
	}
	
	
	private Message waitForResponse() throws PacketException {
		byte data[] = new byte[this.BUFFER_SIZE];
		DatagramPacket receivePacket = new DatagramPacket(data, data.length);

		try {
			this.socket.receive(receivePacket);

		} catch(IOException e) {
			e.printStackTrace();
			throw new PacketException("Network failure.");
		}
		
		return this.bytesToMsg(data);
	}
	

	private byte[] buildMessageBytes(Message msg) {
		byte[] messageBytes = msg.getBytes();
		byte[] encodedBytes = new byte[Constants.INT_SIZE + messageBytes.length];

		int index = 0;
		for (byte b : Functions.intToByteArray(msg.getRequestType())) {
			encodedBytes[index++] = b;
		}
		for (byte b : messageBytes) {
			encodedBytes[index++] = b;
		}
		return encodedBytes;
	}
	
	
	private Message bytesToMsg(byte[] bytes) {
		return new Message(0, bytes);
	}
}
