package src.main.scheduler.elevator_fsm;

import java.net.*;

import src.main.net.*;

/**
 * 
 * ====================== MESSAGES =========================
 * 
 * INPUTS
 *   MSG_ELEVATOR_BUTTON_PRESSED 	- Adds a destination to the elevator specified
 *   MSG_FLOOR_BUTTON_PRESSED		- Adds a destination to an arbitrary elevator
 *   MSG_FLOOR_SENSOR				- Update the current level of an elevator
 *   
 * OUTPUTS
 *   MSG_OPEN_DOORS					- Instruct elevator to open doors
 *   MSG_CLOSE_DOORS				- Instruct elevator to close doors
 *   MSG_MOTOR_UP					- Instruct elevator to turn motor off
 *   MSG_MOTOR_DOWN					- Instruct elevator to turn motor on going down
 *   MSG_MOTOR_STOP					- Instruct elevator to turn motor on going up
 *   MSG_CLEAR_ELEVATOR_BUTTON		- Instruct elevator to turn off the light for a button
 *   MSG_CLEAR_FLOOR_BUTTON			- Instruct floor to turn off the light for a button
 * 
 * @author austinjturner
 *
 */
public class SchedulerSubsystem extends Thread {
	
	private Requester requester;
	private Responder responder;
	
	private InetAddress address;
	
	private StateMachine stateMachine;
	
	private static final int ELEVATOR_PORT = 8001;		// FIXME
	private static final int SCHEDULER_PORT = 8002;	// FIXME
	
	
	public SchedulerSubsystem(Requester requester, Responder responder) {
		this.requester = requester;
		this.responder = responder;
		this.stateMachine = new StateMachine(0, this);
		
		try {
			this.address = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public SchedulerSubsystem() {
		this(new Requester(), new Responder(SCHEDULER_PORT));
	}
	
	public StateMachine getStateMachine() {
		return this.stateMachine;
	}
	
	/**
	 * Starts the scheduler, bind 2 ports and start
	 * receiving requests
	 */
	public void run() {

		for (;;) {
			messageHandle(this.responder.receive());
		}
	}
	
	/**
	 * Calls the appropriate method given the Message's RequestType
	 * 
	 * @param rm
	 */
	private void messageHandle(RequestMessage rm) {
		int requestType = rm.getRequestType();
		
		switch (requestType) {
		
		case MessageAPI.MSG_ELEVATOR_BUTTON_PRESSED:
			handleElevatorButtonPressedMessage(rm);
			break;
		case MessageAPI.MSG_FLOOR_BUTTON_PRESSED:
			handleFloorButtonPressedMessage(rm);
			break;
		case MessageAPI.MSG_CURRENT_FLOOR:
			handleFloorSensorMessage(rm);
			break;
		default:
			System.out.println("ERROR: Unexpected message received by sheduler");
		}
		
		rm.sendResponse(new Message(MessageAPI.MSG_EMPTY_RESPONSE));
	}
	

	private void handleElevatorButtonPressedMessage(RequestMessage rm) {
		// This will need to feed into a scheduling algorithm
	}
	
	private void handleFloorButtonPressedMessage(RequestMessage rm) {
		// This will need to feed into a scheduling algorithm
	}
	
	private void handleFloorSensorMessage(RequestMessage rm) {
		// This will update the elevators floor number.
		// If the elevator has reached it's destination, more
		// actions will occur.
		this.stateMachine.elevatorReachedFloor(rm.getValue());
	}
	
	
	/**
	 * Generic sendMessage to handle errors.
	 * Currently just printStackTrace
	 * 
	 * @param addr
	 * @param port
	 * @param msg
	 */
	private void sendMessage(InetAddress addr, int port, Message msg) {
		try {
			this.requester.sendRequest(addr, port, msg);
		} catch (PacketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendOpenDoorMessage(int elevatorID) {
		sendMessage(this.address, ELEVATOR_PORT, new Message(MessageAPI.MSG_OPEN_DOORS));
	}
	
	public void sendCloseDoorMessage(int elevatorID) {
		sendMessage(this.address, ELEVATOR_PORT, new Message(MessageAPI.MSG_CLOSE_DOORS));
	}
	
	public void sendMotorUpMessage(int elevatorID) {
		sendMessage(this.address, ELEVATOR_PORT, new Message(MessageAPI.MSG_MOTOR_UP));
	}
	
	public void sendMotorDownMessage(int elevatorID) {
		sendMessage(this.address, ELEVATOR_PORT, new Message(MessageAPI.MSG_MOTOR_DOWN));
	}
	
	public void sendMotorStopMessage(int elevatorID) {
		sendMessage(this.address, ELEVATOR_PORT, new Message(MessageAPI.MSG_MOTOR_STOP));
	}
	
	public void sendClearElevatorButtonMessage(int elevatorID, int floorNum) {
		sendMessage(this.address, ELEVATOR_PORT, 
				new Message(MessageAPI.MSG_CLEAR_ELEVATOR_BUTTON, floorNum));
	}
	
	public void sendClearFloorButtonMessage(int elevatorID, int floorNum, boolean goingUp) {
		// TODO
		//sendMessage(this.address, ELEVATOR_PORT, 
		//		new Message(MessageAPI.MSG_CLEAR_ELEVATOR_BUTTON, floorNum));
	}
}
