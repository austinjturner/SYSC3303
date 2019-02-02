package src.main.elevator;

import java.net.InetAddress;
import java.net.UnknownHostException;

import src.main.net.Common;
import src.main.net.Message;
import src.main.net.MessageAPI;
import src.main.net.PacketException;
import src.main.net.RequestMessage;
import src.main.net.Requester;
import src.main.net.Responder;

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
	
	/**
	 * The constructor of the elevator.
	 * 
	 * @param id The id of the elevator given by the scheduler.
	 * @param portNumber The port number given to the elevator to receive scheduler UDP messages over the network.
	 * @param numberOfFloors The number of floors the elevator operates.
	 */
	public Elevator(int id, int portNumber, int numberOfFloors) {
		this.requester = new Requester();
		this.responder = new Responder(portNumber);
		this.elevatorId = id;
		this.currentFloor = 1;
		this.numberOfFloors = numberOfFloors;
		this.motor = motorState.STOP;
		buttons = new boolean[numberOfFloors];
		lamps = new boolean[numberOfFloors];
	}
	
	/**
	 * For implementing Runnable interface.
	 * Allows elevator to receive and respond to UDP messages indefinitely when run as a thread.
	 */
	@Override
	public void run() {
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
				break;
			case MessageAPI.MSG_OPEN_DOORS:
				openDoor();
				break;
			case MessageAPI.MSG_MOTOR_STOP:
				stop();
				break;
			case MessageAPI.MSG_MOTOR_UP:
				goUp();
				break;
			case MessageAPI.MSG_MOTOR_DOWN:
				goDown();
				break;
			case MessageAPI.MSG_TOGGLE_ELEVATOR_LAMP: 
				toggleLamp(message.getValue());
				break;
			case MessageAPI.MSG_PRESS_ELEVATOR_BUTTON: 
				pressButton(message.getValue());
				break;
			case MessageAPI.MSG_CLEAR_ELEVATOR_BUTTON:  
				clearButton(message.getValue());
				break;
			case MessageAPI.MSG_CURRENT_FLOOR:
				// If a floor is requested for the elevator, response must contain requested floor.
				// Otherwise an empty message can be sent to acknowledge elevator received scheduler message.
				sendEmptyResponse = false; 
				message.sendResponse(new Message(MessageAPI.MSG_CURRENT_FLOOR, currentFloor));
				break;
		}
		
		if(sendEmptyResponse) {
			message.sendResponse(new Message(MessageAPI.MSG_EMPTY_RESPONSE, 0) );
		}
	}
	/**
	 * Interrupts the motor timer thread to stop calls incrementing or decrementing elevator's current floor.
	 */
	public void stop() {
		this.motor = motorState.STOP;
		if(motorTimerThread.isAlive()) {
			motorTimerThread.interrupt();
			System.out.println("Elevator stopped...");
		}	
	}
	
	/**
	 * Creates and starts a motorTimer thread to increment elevator current floor while motor is running.
	 */
	public void goUp() {
		this.motor = motorState.UP;
		motorTimerThread = new Thread(new MotorTimer());
		motorTimerThread.start();
		System.out.println("Going up...");
	}
	/**
	 * Creates and starts a motorTimer thread to decrement elevator current floor while motor is running.
	 */
	public void goDown() {
		this.motor = motorState.DOWN;
		motorTimerThread = new Thread(new MotorTimer());
		motorTimerThread.start();
		System.out.println("Going down...");
	}
	/**
	 * Sets elevator door to  open.
	 */
	public void openDoor() {
		this.doorOpen = true;
		System.out.println("Door opened");
	}
	/**
	 * Closes elevator door
	 */
	public void closeDoor() {
		this.doorOpen = false;
		System.out.println("Door closed");
	}
	/**
	 * toggles the lamp representing a floor within the elevator on or off (true or false).
	 * 
	 * @param lampNum number representing the floor the lamp corresponds to.
	 */
	public void toggleLamp(int lampNum) {
		this.lamps[lampNum - 1] = !this.lamps[lampNum - 1];
		System.out.println("Lamp toggled: " + lampNum + ". Value: " + this.lamps[lampNum - 1]);
	}

	/**
	 * Sets a floor button in the elevator as pressed.
	 * 
	 * @param buttonNum floor button to be set as pressed.
	 */
	public void pressButton(int buttonNum) {
		this.buttons[buttonNum - 1] = true;
		System.out.println("Button pressed: " + buttonNum);
	}
	
	/**
	 * Clears a floor button of being pressed.
	 * 
	 * @param buttonNum floor button to be cleared.
	 */
	public void clearButton(int buttonNum) {
		this.buttons[buttonNum - 1] = false;
		System.out.println("Button cleared: " + buttonNum);
	}
	
	/**
	 * Increments the current floor of the elevator and calls floorChangeAlert.
	 */
	public synchronized void incrementFloor() {
		if(this.currentFloor < this.numberOfFloors) {
			this.currentFloor += 1;
			System.out.println("At floor: " + this.currentFloor);
			floorChangeAlert();
		}
	}
	
	/**
	 * Decrements the current floor of the elevator and calls floorChangeAlert.
	 */
	public synchronized void decrementFloor() {
		if(this.currentFloor > 1) {
			this.currentFloor -= 1;
			System.out.println("At floor: " + this.currentFloor);
			floorChangeAlert();
		}
	}
	
	/**
	 * Sends a message containing the current floor of the elevator to scheduler.
	 */
	public void floorChangeAlert() {
		try {
			this.requester.sendRequest(InetAddress.getLocalHost(), Common.PORT_SCHEDULER_SUBSYSTEM, new Message(MessageAPI.MSG_CURRENT_FLOOR, currentFloor));
		} catch (UnknownHostException e) {
			e.printStackTrace();
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
	 * Getter for array of foor buttons.
	 * 
	 * @return buttons Boolean array of floor buttons corresponding to floors. 
	 */
	public boolean[] getButtons() {
		return this.buttons;
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
					Thread.sleep(4990);
					if(motor == motorState.DOWN) {
						decrementFloor();
					}
					else if (motor == motorState.UP) {
						incrementFloor();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}
}

