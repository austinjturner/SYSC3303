package src.main.net;

import java.net.*;

public class MockRequester extends Requester {
	@Override
	public Message sendRequest(InetAddress addr, int port, Message msg) throws PacketException {
		return new Message(0,0);
	}
}
