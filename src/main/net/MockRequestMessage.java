package src.main.net;

import java.net.*;

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
