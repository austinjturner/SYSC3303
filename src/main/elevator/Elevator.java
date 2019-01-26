package src.main.elevator;

import src.main.net.Message;
import src.main.net.MessageAPI;
import src.main.net.RequestMessage;
import src.main.net.Requester;
import src.main.net.Responder;


public class Elevator implements Runnable {

	public enum motor { UP, DOWN, STOP };
	
	private int elevatorId;
	private int currentFloor;
	private boolean doorOpen;
	private boolean buttons[];
	private boolean lamps[];
	private motor motor;
	private Requester requester;
	private Responder responder;
	
	public Elevator(int id, int portNumber, int numberOfFloors) {
		this.requester = new Requester();
		this.responder = new Responder(portNumber);
		this.elevatorId = id;
		doorOpen = false;
		buttons = new boolean[numberOfFloors];
		lamps = new boolean[numberOfFloors];
		 	
		while(true) {
			messageHandler(this.responder.receive());
		}
	}
	
	public void messageHandler(RequestMessage message) {
		int requestType = message.getRequestType();
		boolean sendEmptyResponse = true;
		switch (requestType) {
			case 2001: // close doors
				closeDoor();
				break;
			case 2002:  // open doors
				openDoor();
				break;
			case 2003:  // stop motor
				stop();
				break;
			case 2004:  // motor up
				goUp();
				break;
			case 2005:  // motor down
				goDown();
				break;
			case 2006:  // toggle button lamp
				toggleLamp(message.getValue());
				break;
			case 2007:  // press button
				pressButton(message.getValue());
				break;
			case 2008:  // clear doors
				clearButton();
				break;
			case 2009:  // requesting current floor
				sendEmptyResponse = false; 
				message.sendResponse(new Message(MessageAPI.MSG_CURRENT_FLOOR, currentFloor));
				break;
		}
		
		if(sendEmptyResponse) {
			message.sendResponse(new Message(MessageAPI.MSG_EMPTY_RESPONSE, 0) );
		}
	}
	
	public void stop() {
		this.motor = motor.STOP;
	}
	
	public void goUp() {
		this.motor = motor.UP;
	}
	
	public void goDown() {
		this.motor = motor.DOWN;
	}
	
	public void openDoor() {
		this.doorOpen = true;
	}
	
	public void closeDoor() {
		this.doorOpen = false;
	}
	
	public void toggleLamp(int lampNum) {
		this.lamps[lampNum] = !this.lamps[lampNum];
	}
	
	public void pressButton(int buttonNum) {
		this.buttons[buttonNum] = true;
	}
	
	public void clearButton() {
		this.buttons[currentFloor] = false;
	}
	
	public void incrementFloor() {
		this.currentFloor += 1;
	}
	
	public void deccrementFloor() {
		this.currentFloor -= 1;
	}
	
	public int getCurrentFloor() {
		return this.currentFloor;
	}

	@Override
	public void run() {
		// TODO Implement thread to calculate time taken to change floors
	}
	
	
}
