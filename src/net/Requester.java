package src.net;

import java.io.*;
import java.net.*;

/**
 * The Requester class binds is used to send Messages to another part
 * of the sub-system, then return a response Message.
 * When created, it binds to a port (which may be specified).
 * 
 * Calls to sendRequest will use that port to send a UDP/IP packet,
 * then wait for a packet to be returned on it's port.
 * 
 * Once the returned packet it receive, it is turned into a new Message
 * and returned to the caller
 * 
 * @author austinjturner
 *
 */
public class Requester {
	
	private final int BUFFER_SIZE = 1024;
	private DatagramSocket socket;
	
	/**
	 * Create a new Requester
	 */
 	public Requester() {
 		this(-1);
	}
	
 	/**
 	 * Create a new Requester on a specified port
 	 * @param port
 	 */
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
	
	/**
	 * Close the socket
	 */
	public void Close() {
		this.socket.close();
	}
	
	/**
	 * Send a Message and return a Message
	 * 
	 * @param addr
	 * @param port
	 * @param msg
	 * @return
	 * @throws PacketException
	 */
	public Message sendRequest(InetAddress addr, int port, Message msg) throws PacketException {
		
		DatagramPacket sendPacket;
		byte[] bytes = Common.buildMessageBytes(msg);
		
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
	
	
	/**
	 * 
	 * @return
	 * @throws PacketException
	 */
	private Message waitForResponse() throws PacketException {
		byte data[] = new byte[this.BUFFER_SIZE];
		DatagramPacket receivePacket = new DatagramPacket(data, data.length);

		try {
			this.socket.receive(receivePacket);

		} catch(IOException e) {
			e.printStackTrace();
			throw new PacketException("Network failure.");
		}
		
		return Common.bytesToMsg(data);
	}
}
