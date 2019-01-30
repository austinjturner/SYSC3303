package src.test.elevator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import src.main.elevator.Elevator;
import src.main.elevator.Elevator.motor;
import src.main.net.Message;
import src.main.net.MessageAPI;
import src.main.net.MockRequestMessage;
import src.main.net.RequestMessage;

/*
 * @author nikolaerak
 */
public class TestElevator {

	private Elevator elevator;
	
	@Before
	public void setup() {
		elevator = new Elevator(1,66,6);
	}


	/*
	 * Test to check if elevator door is opening and closing correctly
	 */
	@Test
	public void messageHandler_testDoorOpenAndClose() {

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

		assertEquals(motor.STOP, elevator.getMotorDirection());

		RequestMessage message = new MockRequestMessage(MessageAPI.MSG_MOTOR_UP,0);
		elevator.messageHandler(message);

		assertEquals(motor.UP, elevator.getMotorDirection());

		message = new MockRequestMessage(MessageAPI.MSG_MOTOR_STOP,0);
		elevator.messageHandler(message);

		assertEquals(motor.STOP, elevator.getMotorDirection());

		message = new MockRequestMessage(MessageAPI.MSG_MOTOR_DOWN,0);
		elevator.messageHandler(message);

		assertEquals(motor.DOWN, elevator.getMotorDirection());
	}

	
	/*
	 * Test to check if lamps are toggling correctly
	 */
	@Test
	public void messageHandler_testToggleLamp() {
		//toggle lamp for floor 3
		RequestMessage message = new MockRequestMessage(MessageAPI.MSG_TOGGLE_ELEVATOR_LAMP,3);
		elevator.messageHandler(message);

		assertEquals(true, elevator.getLamps()[3]);

		message = new MockRequestMessage(MessageAPI.MSG_TOGGLE_ELEVATOR_LAMP,3);
		elevator.messageHandler(message);

		assertEquals(false, elevator.getLamps()[3]);
	}

	
	/*
	 * Test to check if buttons are being pressed and cleared
	 */
	@Test
	public void messageHandler_testPressButtonAndClearButton() {
		//press button for floor 3
		RequestMessage message = new MockRequestMessage(MessageAPI.MSG_FLOOR_BUTTON_PRESSED,3);
		elevator.messageHandler(message);

		assertEquals(true, elevator.getButtons()[3]);

		message = new MockRequestMessage(MessageAPI.MSG_CLEAR_ELEVATOR_BUTTON,3);
		elevator.messageHandler(message);

		assertEquals(false, elevator.getButtons()[3]);
	}
}
