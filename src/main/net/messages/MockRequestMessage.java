package src.main.net.messages;

import java.net.*;

import src.main.net.Common;
import src.main.net.Responder;

/**
 * This class can be used to send mock responses with requiring
 * any other subsystem to be running.
 * 
 * @author austinjturner
 *
 */
public class MockRequestMessage extends RequestMessage {
	
	static class MockResponder extends Responder {
		public void sendResponse(DatagramPacket request, Message msg) {
			// Don't send response
			
		}
	}
	
	public MockRequestMessage(int requestType, int value) {
		super(
				new DatagramPacket(
						Common.intToByteArray(value),
						Common.BYTES_PER_INT), 
				new MockResponder());
		this.requestType = requestType;
		this.data = Common.intToByteArray(value);
	}
}
