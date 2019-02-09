package src.test.net;

import static org.junit.Assert.assertEquals;

import java.net.*;

import org.junit.Test;

import src.main.net.*;

public class TestFloorButtonMessage {
	
	@Test
	public void testFloorButton() {
		// Initialize Requester and Responder to test
		Requester requester = new Requester();
		Responder responder = new Responder();
		
		// Some values we will check are received correctly
		int pickUpFloorNum = 6;
		int dropOffFloorNum = 7;
		boolean goingUp = true;
		
		/**
		 * This is the basic model for a server. It calls receive() in a loop,
		 * then does a switch on the request type.
		 * 
		 * This class will cause the test to fail if either
		 *   - The messageType of the receive message is not correct
		 *   - The value of the receive message is not correct 
		 */
		class TestServer extends Thread {
			public void run(){
				for (;;) {
					RequestMessage msgIn = responder.receive();
					switch (msgIn.getRequestType()) {
					
					case MessageAPI.MSG_FLOOR_BUTTON_PRESSED:
						FloorButtonMessage fbm = new FloorButtonMessage(msgIn);
						assertEquals(fbm.getPickUpFloorNumber(), pickUpFloorNum);
						assertEquals(fbm.getDropOffFloorNumber(), dropOffFloorNum);
						assertEquals(fbm.getGoingUp(), goingUp);
						break;
					}
					
					assertEquals(MessageAPI.MSG_FLOOR_BUTTON_PRESSED, msgIn.getRequestType());
					
					msgIn.sendResponse(new Message(MessageAPI.MSG_TEST_SEND_RESPONSE));
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
				Message msgIn = new FloorButtonMessage(pickUpFloorNum, dropOffFloorNum, goingUp);
				Message msgOut = null;
				try {
					msgOut = requester.sendRequest(InetAddress.getLocalHost(), responder.getPort(), msgIn);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (PacketException e) {
					e.printStackTrace();
				}

				assertEquals(MessageAPI.MSG_TEST_SEND_RESPONSE, msgOut.getRequestType());
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
