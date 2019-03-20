package src.main.scheduler;

import java.net.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import src.main.net.*;
import src.main.net.MessageAPI.FaultType;
import src.main.net.messages.*;
import src.main.scheduler.algorithms.*;
import src.main.settings.Settings;

/**
 * 
 * ====================== MESSAGES =========================
 * 
 * INPUTS
 *   MSG_ELEVATOR_BUTTON_PRESSED 	- Adds a destination to the elevator specified
 *   MSG_FLOOR_BUTTON_PRESSED		- Adds a destination to an arbitrary elevator
 *   MSG_FLOOR_SENSOR				- Update the current level of an elevator
 *   MSG_ELEVATOR_STARTED			- Record new elevator ID and port
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
	private Map<Integer, Integer> elevatorPortMap;			// Key is elevatorID, values are portNumber
	private Map<Integer, StateMachine> stateMachineMap;		// Key is elevatorID, values are stateMachine
	
	public SchedulerSubsystem(Requester requester, Responder responder) {
		this.requester = requester;
		this.responder = responder;
		this.elevatorPortMap = new HashMap<Integer, Integer>();
		this.stateMachineMap = new HashMap<Integer, StateMachine>();
		//this.algorithm = new DefaultAlgorithm(this.stateMachineMap);
		this.algorithm = new ShortestLengthToCompleteAlgorithm(this.stateMachineMap);
	}
	
	public SchedulerSubsystem() {
		this(new Requester(), new Responder(Common.PORT_SCHEDULER_SUBSYSTEM));
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
	protected void messageHandle(RequestMessage rm) {

		int requestType = rm.getRequestType();
		switch (requestType) {
		
		case MessageAPI.MSG_FLOOR_BUTTON_PRESSED:
			handleFloorButtonPressedMessage(new FloorButtonPressMessage(rm));
			break;
		case MessageAPI.MSG_CURRENT_FLOOR:
			handleFloorSensorMessage(new ElevatorMessage(rm));
			break;
		case MessageAPI.MSG_ELEVATOR_STARTED:
			handleElevatorStartedMessage(new ElevatorMessage(rm));
			break;
		default:
			print("ERROR: Unexpected message received by scheduler");
		}
		
		
		rm.sendResponse(new Message(MessageAPI.MSG_EMPTY_RESPONSE));
	}
	
	public Map<Integer, StateMachine> getStateMachineMap() {
		return this.stateMachineMap;
	}
	
	
	private void handleFloorButtonPressedMessage(FloorButtonPressMessage fbm) {
		// This will need to feed into a scheduling algorithm
		debug("Got new floor button press: [pickUp = " + fbm.getPickUpFloorNumber() +
				"] [dropOff = " + fbm.getDropOffFloorNumber() + "]");
		
		debug(fbm.hasFault() ? 
				"FaultType: " + fbm.getFaultType() + "  floorNumber: "  + fbm.getFaultFloorNumber() 
				: "No fault");
		
		this.algorithm.handleFloorButtonEvent(
				fbm.getPickUpFloorNumber(), fbm.getDropOffFloorNumber(), fbm.getGoingUp(),
				fbm.getFaultType(), fbm.getFaultFloorNumber());
	}
	
	private void handleFloorSensorMessage(ElevatorMessage em) {
		// This will update the elevators floor number.
		// If the elevator has reached it's destination, more
		// actions will occur.
		//this.stateMachine.elevatorReachedFloorEvent(rm.getValue());
		this.algorithm.handleFloorSensorEvent(em.getValue(), em.getElevatorID());
	}
	
	private void handleElevatorStartedMessage(ElevatorMessage em) {
		// Should we check if elevator already exists?
		this.elevatorPortMap.put(em.getElevatorID(), em.getValue());
		this.stateMachineMap.put(em.getElevatorID(), new StateMachine(em.getElevatorID(), this));
	}
	
	public void checkElevatorWaiting() {
		this.algorithm.handleElevatorWaiting();
	}
	
	
	/**
	 * Generic sendMessage to handle errors.
	 * Currently just printStackTrace
	 * 
	 * @param addr
	 * @param port
	 * @param msg
	 */
	private Message sendMessage(InetAddress addr, int port, Message msg) {
		try {
			return this.requester.sendRequest(addr, port, msg);
		} catch (PacketException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void sendOpenDoorMessage(int elevatorID) {
		sendMessage(Common.IP_ELEVATOR_SUBSYSTEM, elevatorPortMap.get(elevatorID), new Message(MessageAPI.MSG_OPEN_DOORS));
	}
	
	public void sendCloseDoorMessage(int elevatorID) {
		sendMessage(Common.IP_ELEVATOR_SUBSYSTEM, elevatorPortMap.get(elevatorID), new Message(MessageAPI.MSG_CLOSE_DOORS));
	}
	
	public void sendMotorUpMessage(int elevatorID) {
		sendMessage(Common.IP_ELEVATOR_SUBSYSTEM, elevatorPortMap.get(elevatorID), new Message(MessageAPI.MSG_MOTOR_UP));
	}
	
	public void sendMotorDownMessage(int elevatorID) {
		sendMessage(Common.IP_ELEVATOR_SUBSYSTEM, elevatorPortMap.get(elevatorID), new Message(MessageAPI.MSG_MOTOR_DOWN));
	}
	
	public void sendMotorStopMessage(int elevatorID) {
		sendMessage(Common.IP_ELEVATOR_SUBSYSTEM, elevatorPortMap.get(elevatorID), new Message(MessageAPI.MSG_MOTOR_STOP));
	}
	
	public void sendClearElevatorButtonMessage(int elevatorID, int floorNum) {
		sendMessage(Common.IP_ELEVATOR_SUBSYSTEM, elevatorPortMap.get(elevatorID), 
				new Message(MessageAPI.MSG_TURN_OFF_ELEVATOR_LAMP, floorNum));
	}
	
	public void sendSetElevatorButtonMessage(int elevatorID, int floorNum) {
		sendMessage(Common.IP_ELEVATOR_SUBSYSTEM, elevatorPortMap.get(elevatorID), 
				new Message(MessageAPI.MSG_TURN_ON_ELEVATOR_LAMP, floorNum));
	}
	
	public void sendSimulateFaultMessage(int elevatorID, int floorNum, FaultType faultType) {
		sendMessage(Common.IP_ELEVATOR_SUBSYSTEM, elevatorPortMap.get(elevatorID), 
				new SimulateFaultMessage(faultType, floorNum));
	}
	
	public void sendClearFloorButtonMessage(int floorNum, boolean goingUp) {
		sendMessage(Common.IP_FLOOR_SUBSYSTEM, Common.PORT_FLOOR_SUBSYSTEM, 
				new FloorButtonClearMessage(floorNum, goingUp));
	}
	
	public void sendShutdownElevatorMessage(int elevatorID) {
		sendMessage(Common.IP_ELEVATOR_SUBSYSTEM, elevatorPortMap.get(elevatorID), new Message(MessageAPI.MSG_SHUTDOWN_ELEVATOR));
	}
	
	/**
	 * 
	 * @param elevatorID
	 * @return boolean indicating whether doors are open
	 */
	public int sendGetElevatorDoorsStateMessage(int elevatorID) {
		Message msg = sendMessage(Common.IP_ELEVATOR_SUBSYSTEM, elevatorPortMap.get(elevatorID), new Message(MessageAPI.MSG_GET_DOORS_STATE));
		return msg.getValue();		// 0 => doors are open   1 => doors are closed
	}
	
	
	public boolean areElevatorDoorsClosed(int elevatorID) {
		return sendGetElevatorDoorsStateMessage(elevatorID) == 1;
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
