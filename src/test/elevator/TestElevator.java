package src.test.elevator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import src.main.elevator.Elevator;
import src.main.elevator.Elevator.motorState;
import src.main.net.MessageAPI;
import src.main.net.MockRequester;
import src.main.net.Requester;
import src.main.net.Responder;
import src.main.net.messages.Message;
import src.main.net.messages.MockRequestMessage;
import src.main.net.messages.RequestMessage;

/*
 * @author nikolaerak
 */
public class TestElevator {

	private Elevator elevator;
	private int portNumber = 1234;
	

	/*
	 * Test to check if elevator door is opening and closing correctly
	 */
	@Test
	public void messageHandler_testDoorOpenAndClose() {

		elevator = new Elevator(1,portNumber,6);
		assertEquals(false, elevator.isDoorOpen());
		RequestMessage message = new MockRequestMessage(MessageAPI.MSG_OPEN_DOORS,0);
		elevator.messageHandler(message);
		assertEquals(true, elevator.isDoorOpen());
		message = new MockRequestMessage(MessageAPI.MSG_CLOSE_DOORS,0);
		elevator.messageHandler(message);
		assertEquals(false, elevator.isDoorOpen());
	}

	
	/*
	 * Test to check if motor directon is changing correctly
	 */
	@Test
	public void messageHandler_testMotorUpMotorDownAndStop() {

		elevator = new Elevator(1,portNumber + 1,6);
		assertEquals(motorState.STOP, elevator.getMotorDirection());

		RequestMessage message = new MockRequestMessage(MessageAPI.MSG_MOTOR_UP,0);
		elevator.messageHandler(message);

		assertEquals(motorState.UP, elevator.getMotorDirection());
		
		message = new MockRequestMessage(MessageAPI.MSG_MOTOR_STOP,0);
		elevator.messageHandler(message);

		assertEquals(motorState.STOP, elevator.getMotorDirection());

		message = new MockRequestMessage(MessageAPI.MSG_MOTOR_DOWN,0);
		elevator.messageHandler(message);

		assertEquals(motorState.DOWN, elevator.getMotorDirection());
	}

	
	/*
	 * Test to check if lamps are toggling correctly
	 */
	@Test
	public void messageHandler_testToggleLamp() {

		elevator = new Elevator(1,portNumber + 2,6);
		//toggle lamp for floor 3
		RequestMessage message = new MockRequestMessage(MessageAPI.MSG_TURN_ON_ELEVATOR_LAMP,3);
		elevator.messageHandler(message);
		assertEquals(true, elevator.getLamps()[2]);

		message = new MockRequestMessage(MessageAPI.MSG_TURN_OFF_ELEVATOR_LAMP,3);
		elevator.messageHandler(message);

		assertEquals(false, elevator.getLamps()[2]);
	}

	
	/*
	 * Test to check if buttons are being pressed and cleared
	 */
	@Test
	public void messageHandler_testPressButtonAndClearButton() {
		
		elevator = new Elevator(1,portNumber + 3,6);
		//press button for floor 3
		RequestMessage message = new MockRequestMessage(MessageAPI.MSG_PRESS_ELEVATOR_BUTTON,3);
		elevator.messageHandler(message);

		assertEquals(true, elevator.getButtons()[2]);

		message = new MockRequestMessage(MessageAPI.MSG_CLEAR_ELEVATOR_BUTTON,3);
		elevator.messageHandler(message);

		assertEquals(false, elevator.getButtons()[2]);
	}
	
	/*
	 * Test to check if buttons are being pressed and cleared
	 */
	@Test
	public void messageHandler_testIncrementAndDecrementFloor() {
		MockRequester mockRequester = new MockRequester();
		Responder responder = new Responder();
		elevator = new Elevator(1,portNumber + 4,2, mockRequester, responder);
		assertEquals(1, elevator.getCurrentFloor());
		
		//should not be able to decrement
		elevator.decrementFloor();
		assertEquals(1, elevator.getCurrentFloor());

		elevator.incrementFloor();
		assertEquals(2, elevator.getCurrentFloor());
		
		//should not be able to increment anymore
		elevator.incrementFloor();
		assertEquals(2, elevator.getCurrentFloor());
	}
}
