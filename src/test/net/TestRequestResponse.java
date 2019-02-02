package src.test.net;

import static org.junit.Assert.assertEquals;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Test;

import src.main.net.Message;
import src.main.net.MessageAPI;
import src.main.net.PacketException;
import src.main.net.RequestMessage;
import src.main.net.Requester;
import src.main.net.Responder;

public class TestRequestResponse {

	private int testPort = 51515;
	
	/**
	 * This test just checks that ports are being opened and closed correctly
	 */
	@Test
	public void testResponderBindsAndReleasesPort() {
		// Test open port
		Responder responder = new Responder(testPort);
		assertEquals(responder.getPort(), testPort);
		
		// Test close port
		responder.close();
		responder = new Responder(testPort);
		assertEquals(responder.getPort(), testPort);
		responder.close();
	}
	
	
	/**
	 * This test ensures that message passed between a TestClient and TestServer are
	 * propagates without error.
	 * 
	 * 
	 */
	@Test
	public void testRequestReponse() {
		// Initialize Requester and Responder to test
		Requester requester = new Requester();
		Responder responder = new Responder();
		
		// Some values we will check are received correctly
		int testValue1 = 12345;
		int testValue2 = 67890;
		
		/**
		 * This is the basic model for a server. It calls receive() in a loop,
		 * then does a switch on the request type.
		 * 
		 * This class will cause the test to fail if either
		 *   - The messageType of the receive message is not correct
		 *   - The value of the receive message is not correct 
		 */
		class TestServer extends Thread{
			public void run(){
				for (;;) {
					RequestMessage msgIn = responder.receive();
					switch (msgIn.getRequestType()) {
					
					case MessageAPI.MSG_TEST_SEND_REQUEST:
						assertEquals(testValue1, msgIn.getValue());
						break;
						
					default:
						assertEquals(MessageAPI.MSG_TEST_SEND_REQUEST, msgIn.getRequestType());
					}
					assertEquals(MessageAPI.MSG_TEST_SEND_REQUEST, msgIn.getRequestType());
					assertEquals(testValue1, msgIn.getValue());
					
					Message msgOut = new Message(MessageAPI.MSG_TEST_SEND_RESPONSE, testValue2);
					msgIn.sendResponse(msgOut);
				}
			}
		}
		
		/**
		 * This is the basic model for a client. It calls sendRequest(), then waits for the
		 * Responder to send the response packet. That is converted to a Message and returned
		 * from sendRequest()
		 * 
		 * This class will cause the test to fail if either
		 *   - The messageType of the receive message is not correct
		 *   - The value of the receive message is not correct 
		 */
		class TestClient extends Thread{
			public void run(){
				Message msgIn = new Message(MessageAPI.MSG_TEST_SEND_REQUEST, testValue1);
				Message msgOut = null;
				try {
					msgOut = requester.sendRequest(InetAddress.getLocalHost(), responder.getPort(), msgIn);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (PacketException e) {
					e.printStackTrace();
				}

				assertEquals(MessageAPI.MSG_TEST_SEND_RESPONSE, msgOut.getRequestType());
				assertEquals(testValue2, msgOut.getValue());
			}
		}
		
		// Run our threads
		Thread testServer = new TestServer();
		testServer.start();
		Thread testClient = new TestClient();
		testClient.start();
		
		// Once the testClient has return, the test is complete
		try {
			testClient.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
