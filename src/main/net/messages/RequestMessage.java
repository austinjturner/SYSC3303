package src.main.net.messages;

import java.net.*;

import src.main.net.Common;
import src.main.net.Responder;

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
	
	public RequestMessage(DatagramPacket packet, Responder responder) {
		super(0, 0);
		
		this.responder = responder;
		this.receivePacket = packet;
		byte[] msg = packet.getData();
		
		this.requestType = Common.byteArrayToInt(new byte[]{msg[0], msg[1], msg[2], msg[3]});
		byte[] data = new byte[packet.getLength() - Common.BYTES_PER_INT];
		for (int i = 0; i < data.length; i++) {
			data[i] = msg[i + Common.BYTES_PER_INT];
		}
		this.data = data;
	}
	
	public void sendResponse(Message respMsg) {
		this.responder.sendResponse(this.receivePacket, respMsg);
	}
}
