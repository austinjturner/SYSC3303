package src.main.elevator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import src.main.net.*;
import src.main.net.MessageAPI.FaultType;
import src.main.net.messages.*;
import src.main.settings.Settings;

/**
 * Represents an elevator with common functionality to go up and down floors,
 * open and close door, acceess floors via buttons.
 * Communicates with the scheduler subsystem to operate.
 * 
 * @author Sam English
 * @author nikolaerak
 *
 */
public class Elevator implements Runnable {

	public enum motorState { UP, DOWN, STOP };
	private int elevatorId;
	private int currentFloor;
	private boolean doorOpen;
	private boolean buttons[];
	private boolean lamps[];
	private motorState motor;
	private Requester requester;
	private Responder responder;
	private Thread motorTimerThread;
	private int numberOfFloors;
	
	// Elevator fault simulation
	private boolean simulateStopFault;
	private boolean simulateDoorClosedFault;
	private boolean simulateDoorOpenFault;
	int simulatedFaultFloor;
	
	/**
	 * Constructor of the elevator.
	 * 
	 * @param id The id of the elevator given by the scheduler.
	 * @param portNumber The port number given to the elevator to receive scheduler UDP messages over the network.
	 * @param numberOfFloors The number of floors the elevator operates.
	 */
	public Elevator(int id, int portNumber, int numberOfFloors) {
		this(id, portNumber, numberOfFloors, 
				new Requester(), new Responder(portNumber));
	}
	
/**
 * Constructor of the elevator, usable for testing with passing in a MockRequester and Responder.
 * 
 * @param id The id of the elevator given by the scheduler.
 * @param portNumber The port number given to the elevator to receive scheduler UDP messages over the network.
 * @param numberOfFloors The number of floors the elevator operates.
 * @param requester MockRequester for testing purposes so elevator does not hang waiting for responses.
 * @param responder Responder for testing purposes
 */
	public Elevator(int id, int portNumber, int numberOfFloors, 
			Requester requester, Responder responder) {
		
		this.requester = requester;
		this.responder = responder;
		this.elevatorId = id;
		this.currentFloor = 1;
		this.numberOfFloors = numberOfFloors;
		this.motor = motorState.STOP;
		buttons = new boolean[numberOfFloors];
		lamps = new boolean[numberOfFloors];
	}
	
	/**
	 * For implementing Runnable interface.
	 * On thread creation, sends a message to the scheduler to receive elevator's id, port and address.
	 * Allows elevator to receive and respond to UDP messages indefinitely when run as a thread.
	 */
	@Override
	public void run() {
		try {
			this.requester.sendRequest(
					Common.IP_SCHEDULER_SUBSYSTEM, Common.PORT_SCHEDULER_SUBSYSTEM, 
					new ElevatorMessage(MessageAPI.MSG_ELEVATOR_STARTED, elevatorId, this.responder.getPort()));
		} catch (PacketException e) {
			e.printStackTrace();
		}
		while(true) {
			messageHandler(this.responder.receive());
		}
	}
	
	
	/**
	 * Interprets message received by elevator according to MessageAPI and performs corresponding action(s).
	 * 
	 * @param message UDP message to be interpreted.
	 */
	public void messageHandler(RequestMessage message) {
		int requestType = message.getRequestType();
		boolean sendEmptyResponse = true;
		// Going through all possible cases for request messages, calling appropriate methods in response
		switch (requestType) {
			case MessageAPI.MSG_CLOSE_DOORS: 
				closeDoor();
				message.sendResponse(new ElevatorMessage(requestType, elevatorId, 0));
				break;
			case MessageAPI.MSG_OPEN_DOORS:
				openDoor();
				message.sendResponse(new ElevatorMessage(requestType, elevatorId,0));
				break;
			case MessageAPI.MSG_MOTOR_STOP:
				stop();
				message.sendResponse(new ElevatorMessage(requestType, elevatorId, 0));
				break;
			case MessageAPI.MSG_MOTOR_UP:
				goUp();
				message.sendResponse(new ElevatorMessage(requestType, elevatorId, 0));
				break;
			case MessageAPI.MSG_MOTOR_DOWN:
				goDown();
				message.sendResponse(new ElevatorMessage(requestType, elevatorId, 0));
				break;
			case MessageAPI.MSG_TURN_OFF_ELEVATOR_LAMP: 
				turnOffLamp(message.getValue());
				message.sendResponse(new ElevatorMessage(requestType, elevatorId, 0));
				break;
			case MessageAPI.MSG_TURN_ON_ELEVATOR_LAMP:
				turnOnLamp(message.getValue());
				message.sendResponse(new ElevatorMessage(requestType, elevatorId, 0));
				break;
			case MessageAPI.MSG_PRESS_ELEVATOR_BUTTON: 
				pressButton(message.getValue());
				message.sendResponse(new ElevatorMessage(requestType, elevatorId, 0));
				break;
			case MessageAPI.MSG_CLEAR_ELEVATOR_BUTTON:  
				clearButton(message.getValue());
				message.sendResponse(new ElevatorMessage(requestType, elevatorId, 0));
				break;
			case MessageAPI.MSG_SIMULATE_FAULT:  
				SimulateFaultMessage faultMessage = new SimulateFaultMessage(message);
				simulateFault(faultMessage.getFaultType(), faultMessage.getFaultFloorNumber());
				message.sendResponse(new ElevatorMessage(requestType, elevatorId, 0));
				break;
			case MessageAPI.MSG_SHUTDOWN_ELEVATOR:  
				stop();
				message.sendResponse(new ElevatorMessage(requestType, elevatorId, 0));
				break;
			case MessageAPI.MSG_GET_DOORS_STATE:  
				message.sendResponse(new ElevatorMessage(requestType, elevatorId, getDoorState()));
				break;
			case MessageAPI.MSG_CURRENT_FLOOR:
				// If a floor is requested for the elevator, response must contain requested floor.
				// Otherwise an empty message can be sent to acknowledge elevator received scheduler message.
				sendEmptyResponse = false; 
				message.sendResponse(new ElevatorMessage(requestType, elevatorId, currentFloor));
				break;
		}
		
		if(sendEmptyResponse) {
			message.sendResponse(new ElevatorMessage(MessageAPI.MSG_EMPTY_RESPONSE, elevatorId, 0));
		}
	}
	
	private void simulateFault(FaultType fault, int faultFloorNumber) {
		this.simulatedFaultFloor = faultFloorNumber;
		if(fault == FaultType.ElevatorFailedToStop) {
			this.simulateStopFault = true;
		}
		else if(fault == FaultType.ElevatorFailedToCloseDoors) {
			this.simulateDoorClosedFault = true;
		}
		else if(fault == FaultType.ElevatorFailedToOpenDoors){
			this.simulateDoorOpenFault = true;
		}
		
	}

	/**
	 * Returns value of door state, being open (1) or closed (0)
	 * 
	 * @return int value for door state
	 */
	private int getDoorState() {
		return this.doorOpen ? 0: 1;
	}
	
	/**
	 * Interrupts the motor timer thread to stop calls incrementing or decrementing elevator's current floor.
	 */
	public void stop() {
		
		if(simulateStopFault == true && simulatedFaultFloor == currentFloor){
			simulateStopFault = false;
			return;
		}
		
		this.motor = motorState.STOP;
		if(motorTimerThread.isAlive()) {
			motorTimerThread.interrupt();
			print("Stopped...");
		}
	}
	
	/**
	 * Creates and starts a motorTimer thread to increment elevator current floor while motor is running.
	 */
	public void goUp() {
		this.motor = motorState.UP;
		motorTimerThread = new Thread(new MotorTimer());
		motorTimerThread.start();
		print("Going up...");
	}
	
	/**
	 * Creates and starts a motorTimer thread to decrement elevator current floor while motor is running.
	 */
	public void goDown() {
		this.motor = motorState.DOWN;
		motorTimerThread = new Thread(new MotorTimer());
		motorTimerThread.start();
		print("Going down...");
	}
	
	/**
	 * Sets elevator door to  open.
	 */
	public void openDoor() {
		
		if(simulateDoorOpenFault == true && simulatedFaultFloor == currentFloor){
			simulateStopFault = false;
			return;
		}
		
		this.doorOpen = true;
		print("Door opened");
	}
	
	/**
	 * Closes elevator door
	 */
	public void closeDoor() {
		
		if(simulateDoorClosedFault == true && simulatedFaultFloor == currentFloor){
			simulateStopFault = false;
			return;
		}
		
		this.doorOpen = false;
		print("Door closed");
	}
	
	/**
	 * turns on the lamp representing a floor within the elevator (true)
	 * 
	 * @param lampNum number representing the floor the lamp corresponds to.
	 */
	public void turnOnLamp(int lampNum) {
		this.lamps[lampNum - 1] = true;
		print("Lamp turned on: " + lampNum + ". Value: " + this.lamps[lampNum - 1]);
	}
	
	/**
	 * turns off the lamp representing a floor within the elevator (false)
	 * 
	 * @param lampNum number representing the floor the lamp corresponds to.
	 */
	public void turnOffLamp(int lampNum) {
		this.lamps[lampNum - 1] = false;
		print("Lamp turned off: " + lampNum + ". Value: " + this.lamps[lampNum - 1]);
	}

	/**
	 * Sets a floor button in the elevator as pressed.
	 * 
	 * @param buttonNum floor button to be set as pressed.
	 */
	public void pressButton(int buttonNum) {
		this.buttons[buttonNum - 1] = true;
		print("Button pressed: " + buttonNum);
	}
	
	/**
	 * Clears a floor button of being pressed.
	 * 
	 * @param buttonNum floor button to be cleared.
	 */
	public void clearButton(int buttonNum) {
		this.buttons[buttonNum - 1] = false;
		print("Button cleared: " + buttonNum);
	}
	
	/**
	 * Increments the current floor of the elevator and calls floorChangeAlert.
	 */
	public synchronized void incrementFloor() {
		if(this.currentFloor < this.numberOfFloors) {
			this.currentFloor += 1;
			print("At floor: " + this.currentFloor);
			floorChangeAlert();
		}
	}
	
	/**
	 * Decrements the current floor of the elevator and calls floorChangeAlert.
	 */
	public synchronized void decrementFloor() {
		if(this.currentFloor > 1) {
			this.currentFloor -= 1;
			print("At floor: " + this.currentFloor);
			floorChangeAlert();
		}
	}
	
	/**
	 * Sends a message containing the current floor of the elevator to scheduler.
	 */
	public void floorChangeAlert() {
		try {
			this.requester.sendRequest(Common.IP_SCHEDULER_SUBSYSTEM, Common.PORT_SCHEDULER_SUBSYSTEM, new ElevatorMessage(MessageAPI.MSG_CURRENT_FLOOR, elevatorId, currentFloor));
		} catch (PacketException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Getter for the current floor of elevator.
	 * 
	 * @return currentFloor Integer for indicating the current floor.
	 */
	public int getCurrentFloor() {
		return this.currentFloor;
	}
	
	/**
	 * Getter for boolean indicating door open or closed.
	 * 
	 * @return doorOpen Boolean value to check if the elevator door is open.
	 */
	public boolean isDoorOpen() {
		return this.doorOpen;
	}
	
	/**
	 * Getter for state of the elevator motor.
	 * 
	 * @return motor Enum with value of motor state.
	 */
	public motorState getMotorDirection() {
		return this.motor;
	}
	
	/**
	 * Getter for array of elevator button lamps.
	 * 
	 * @return lamps Array of boolean values for lamps corresponding to floors.
	 */
	public boolean[] getLamps() {
		return this.lamps;
	}
	
	/**
	 * Getter for array of door buttons.
	 * 
	 * @return buttons Boolean array of floor buttons corresponding to floors. 
	 */
	public boolean[] getButtons() {
		return this.buttons;
	}
	
    public void print(String s) {
        System.out.println("[" + DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS").format(LocalDateTime.now()) + "][ ELEVATOR " + this.elevatorId + " ] " + s);
    }
    
	
	/**
	 * MotorTimer is used to simulate the movement of an elevator between floors. ]
	 * It implements runnable and is run as a thread during operation of the elevator.
	 * It is created when an elevator motor is requested to go up or down, whereby it
	 * sleeps until the simulated duration of time passes to change floors. It increments
	 * or decrements the current floor accordingly, and repeats this until the elevator is
	 * requested to stop at which point the thread is interrupted and returns.
	 * 
	 * @author Sam English
	 *
	 */
	class MotorTimer implements Runnable {
		
		@Override
		public void run() {
			while (motor != motorState.STOP) {
				try {
					// Simulated floor change time of 4.990 seconds
					Thread.sleep(Settings.TIME_BETWEEN_FLOORS);
					if(motor == motorState.DOWN) {
						decrementFloor();
					}
					else if (motor == motorState.UP) {
						incrementFloor();
					}
				} catch (InterruptedException e) {
					return;
				}
			}
		}
	}
}

