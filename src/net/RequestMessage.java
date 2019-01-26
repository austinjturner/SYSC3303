package src.net;

import java.net.*;

/**
 * A RequestMessage is a Message with the ability to send a response Message
 * back to the origin sender via a call to sendRespone()
 * 
 * @author austinjturner
 *
 */
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
		this.value = Common.byteArrayToInt(new byte[]{msg[4], msg[5], msg[6], msg[7]});
	}
	
	public void sendResponse(Message respMsg) {
		this.responder.sendResponse(this.receivePacket, respMsg);
	}
}
