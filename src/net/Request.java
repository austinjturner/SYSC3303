package net;
import java.net.DatagramPacket;

import common.*;

public class Request {
	private Responder responder;
	private int requestID;
	private DatagramPacket receivePacket;
	private Message msg;
	
	public Message GetMessage() {
		return this.msg;
	}
	
	public int getRequestID() {
		return this.requestID;
	}
	
	public void sendResponse(Message respMsg) {
		this.responder.sendResponse(this.receivePacket, respMsg);
	}
	
	public Request(DatagramPacket packet, Responder responder) {
		this.responder = responder;
		this.receivePacket = packet;
		byte[] msg = packet.getData();
		int requestType = Functions.byteArrayToInt(new byte[]{msg[0], msg[1], msg[2], msg[3]});
		byte[] data = new byte[packet.getLength() - Constants.INT_SIZE];
		for (int i = 0; i < data.length; i++) {
			data[i] = msg[i + Constants.INT_SIZE];
		}
		this.msg = new Message(requestType, data);
	}
}
