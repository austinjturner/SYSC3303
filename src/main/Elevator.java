package src.main;

import src.net.Message;
import src.net.Requester;
import src.net.Responder;


public class Elevator {

	public enum motor { UP, DOWN, STOP };
	
	private int elevatorId;
	private int currentFloor;
	private boolean doorsOpen;
	private boolean buttons[];
	private boolean lamps[];
	private motor motor;
	private Requester requester;
	private Responder responder;
	
	public Elevator(int id, int portNumber, int numberOfFloors) {
		this.requester = new Requester();
		this.responder = new Responder(portNumber);
		this.elevatorId = id;
		doorsOpen = false;
		buttons = new boolean[numberOfFloors];
		lamps = new boolean[numberOfFloors];
		 	
		while(true) {
			messageHandler(this.responder.receive());
		}
	}
	
	private void messageHandler(Message message) {
		int requestType = message.getRequestType();
		switch (requestType) {
			case 2001: // close doors
				
				break;
			case 2002:  // open doors
				
				break;
			case 2003:  // stop motor
				
				break;
			case 2004:  // motor up
				
				break;
			case 2005:  // motor down
				
				break;
			case 2006:  // toggle button lamp
				
				break;
			case 2007:  // press button
				
				break;
			case 2008:  // clear doors
				
				break;
			case 2009:  // requesting current floor
				
				break;
		}
	}
	
	private void stop() {
		this.motor = motor.STOP;
	}
	
	private void goUp() {
		this.motor = motor.UP;
	}
	
	private void goDown() {
		this.motor = motor.DOWN;
	}
	
	private void openDoors() {
		this.doorsOpen = true;
	}
	
	private void closeDoor() {
		this.doorsOpen = false;
	}
	
	private void toggleLamp(int lampNum) {
		this.lamps[lampNum] = !this.lamps[lampNum];
	}
	
	private void pressButton(int buttonNum) {
		this.buttons[buttonNum] = true;
	}
	
	private void clearButton() {
		this.buttons[this.currentFloor] = false;
	}
	
	private void incrementFloor() {
		this.currentFloor += 1;
	}
	
	private void deccrementFloor() {
		this.currentFloor -= 1;
	}
	
	private int getCurrentFloor() {
		return this.currentFloor;
	}
	
}
