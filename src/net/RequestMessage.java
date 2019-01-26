package src.net;

import java.net.*;

public class RequestMessage extends Message {
	
	private Responder responder;
	private DatagramPacket receivePacket;
	
	public RequestMessage(int requestType, int value) {
		super(requestType, value);
	}
	
	public RequestMessage(DatagramPacket packet, Responder responder) {
		super(0, 0);
		
		this.responder = responder;
		this.receivePacket = packet;
		byte[] msg = packet.getData();
		
		this.requestType = Common.byteArrayToInt(new byte[]{msg[0], msg[1], msg[2], msg[3]});
		this.value = Common.byteArrayToInt(new byte[]{msg[0], msg[1], msg[2], msg[3]});
	}
	
	public void sendResponse(Message respMsg) {
		this.responder.sendResponse(this.receivePacket, respMsg);
	}
}
