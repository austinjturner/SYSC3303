package src.main.net;

import java.net.*;

/**
 * This class can be used to send mock requests with requiring
 * any other subsystem to be running.
 * 
 * @author austinjturner
 *
 */
public class MockRequester extends Requester {
	
	@Override
	public Message sendRequest(InetAddress addr, int port, Message msg) throws PacketException {
		return new Message(0,0);
	}
}
