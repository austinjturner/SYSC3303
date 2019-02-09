package src.main.scheduler;

import java.net.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import src.main.net.*;
import src.main.settings.Settings;

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

	private Algorithm algorithm;
	private List<StateMachine> stateMachineList;
	
	public SchedulerSubsystem(Requester requester, Responder responder) {
		this.requester = requester;
		this.responder = responder;
		this.stateMachineList = new ArrayList<StateMachine>();
		for (int i = 0; i < Settings.NUMBER_OF_ELEVATOR; i++) {
			this.stateMachineList.add(new StateMachine(i, this));
		}
		this.algorithm = new DefaultAlgorithm(this.stateMachineList);
	}
	
	public SchedulerSubsystem() {
		this(new Requester(), new Responder(Common.PORT_SCHEDULER_SUBSYSTEM));
	}
	
	// FIX ME -- I think this is used in a test?
	public StateMachine getStateMachine() {
		return this.stateMachineList.get(0);
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
		
		case MessageAPI.MSG_FLOOR_BUTTON_PRESSED:
			handleFloorButtonPressedMessage(new FloorButtonPressMessage(rm));
			break;
		case MessageAPI.MSG_CURRENT_FLOOR:
			handleFloorSensorMessage(rm);
			break;
		default:
			print("ERROR: Unexpected message received by scheduler");
		}
		
		rm.sendResponse(new Message(MessageAPI.MSG_EMPTY_RESPONSE));
	}
	
	private void handleFloorButtonPressedMessage(FloorButtonPressMessage fbm) {
		// This will need to feed into a scheduling algorithm
		this.algorithm.handleFloorButtonEvent(
				fbm.getPickUpFloorNumber(), fbm.getDropOffFloorNumber(), fbm.getGoingUp());
	}
	
	private void handleFloorSensorMessage(RequestMessage rm) {
		// This will update the elevators floor number.
		// If the elevator has reached it's destination, more
		// actions will occur.
		//this.stateMachine.elevatorReachedFloorEvent(rm.getValue());
		this.algorithm.handleFloorSensorEvent(rm.getValue(), 0); // FIXME - 0
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
		sendMessage(Common.IP_ELEVATOR_SUBSYSTEM, Common.PORT_ELEVATOR_SUBSYSTEM, new Message(MessageAPI.MSG_OPEN_DOORS));
	}
	
	public void sendCloseDoorMessage(int elevatorID) {
		sendMessage(Common.IP_ELEVATOR_SUBSYSTEM, Common.PORT_ELEVATOR_SUBSYSTEM, new Message(MessageAPI.MSG_CLOSE_DOORS));
	}
	
	public void sendMotorUpMessage(int elevatorID) {
		sendMessage(Common.IP_ELEVATOR_SUBSYSTEM, Common.PORT_ELEVATOR_SUBSYSTEM, new Message(MessageAPI.MSG_MOTOR_UP));
	}
	
	public void sendMotorDownMessage(int elevatorID) {
		sendMessage(Common.IP_ELEVATOR_SUBSYSTEM, Common.PORT_ELEVATOR_SUBSYSTEM, new Message(MessageAPI.MSG_MOTOR_DOWN));
	}
	
	public void sendMotorStopMessage(int elevatorID) {
		sendMessage(Common.IP_ELEVATOR_SUBSYSTEM, Common.PORT_ELEVATOR_SUBSYSTEM, new Message(MessageAPI.MSG_MOTOR_STOP));
	}
	
	public void sendClearElevatorButtonMessage(int elevatorID, int floorNum) {
		sendMessage(Common.IP_ELEVATOR_SUBSYSTEM, Common.PORT_ELEVATOR_SUBSYSTEM, 
				new Message(MessageAPI.MSG_TURN_OFF_ELEVATOR_LAMP, floorNum));
	}
	
	public void sendSetElevatorButtonMessage(int elevatorID, int floorNum) {
		sendMessage(Common.IP_ELEVATOR_SUBSYSTEM, Common.PORT_ELEVATOR_SUBSYSTEM, 
				new Message(MessageAPI.MSG_TURN_ON_ELEVATOR_LAMP, floorNum));
	}
	
	public void sendClearFloorButtonMessage(int floorNum, boolean goingUp) {
		// TODO
		sendMessage(Common.IP_FLOOR_SUBSYSTEM, Common.PORT_FLOOR_SUBSYSTEM, 
				new FloorButtonClearMessage(floorNum, goingUp));
	}
	
	
	public void print(String s) {
		System.out.println("[" + DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS").format(LocalDateTime.now()) + "][ SCHEDULER ] " + s);
	}
	
	public void debug(String s) {
		if (Settings.DEBUG_SCHEDULER) {
			print(s);
		}
	}
}
