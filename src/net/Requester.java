package src.net;

import java.io.*;
import java.net.*;

public class Requester {
	private final int BUFFER_SIZE = 1024;
	private final int BYTES_PER_INT = 4;
	
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
		byte[] encodedBytes = new byte[2 * BYTES_PER_INT];

		int index = 0;
		for (byte b : Common.intToByteArray(msg.getRequestType())) {
			encodedBytes[index++] = b;
		}
		for (byte b : Common.intToByteArray(msg.getValue())) {
			encodedBytes[index++] = b;
		}
		return encodedBytes;
	}
	
	
	private Message bytesToMsg(byte[] b) {
		return new Message(
				Common.byteArrayToInt(new byte[]{b[0], b[1], b[2], b[3]}), 
				Common.byteArrayToInt(new byte[] {b[4], b[5], b[6], b[7]})
		);
	}
	

}
