package src.net;

import java.io.*;
import java.net.*;

/**
 * The Responder class is used to receive packets and return a Message
 * to the sender.
 * 
 * When created Responder
 * 
 * @author austinjturner
 *
 */
public class Responder {
	
	private DatagramSocket socket;
	
	private final int BUFFER_SIZE = 1024;
	
	public int getPort() {
		return this.socket.getLocalPort();
	}
	
 	public Responder() {
 		this(-1);
	}
	
	public Responder(int port) {
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
	
	public void Close() {
		this.socket.close();
	}
	
	public RequestMessage receive() {
		byte data[] = new byte[this.BUFFER_SIZE];
		DatagramPacket receivePacket = new DatagramPacket(data, data.length);

		try {
			this.socket.receive(receivePacket);
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		RequestMessage response = new RequestMessage(receivePacket, this);
		// process
		return response;
	}
	
	public void sendResponse(DatagramPacket request, Message msg) {
		//byte[] bytes = Common.intToByteArray(msg.getValue());
		byte[] bytes = Common.buildMessageBytes(msg);
		DatagramPacket packet = new DatagramPacket(bytes, bytes.length, 
				request.getAddress(), request.getPort());
		try {
			this.socket.send(packet);
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}	
}
