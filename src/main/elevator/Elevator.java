package src.main.elevator;

import java.net.InetAddress;
import java.net.UnknownHostException;

import src.main.net.Message;
import src.main.net.MessageAPI;
import src.main.net.PacketException;
import src.main.net.RequestMessage;
import src.main.net.Requester;
import src.main.net.Responder;


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
	private int addr;
	private int host;
	
// 	TODO remove this later
//	Testing elevator timer thread,
//
//	public static void main(String[] args) {
//		Elevator elevator = new Elevator(1, 69, 6);
//		System.out.println("Starting elevator...");
//		System.out.println("Destination floor: 4");
//		System.out.println("At Floor:" + elevator.getCurrentFloor());
//		elevator.goUp();
//		try {
//			Thread.sleep(15000);
//			elevator.stop();
//		} catch (InterruptedException e) {
//			System.out.println("Main thread interrupted");
//			e.printStackTrace();
//		}
//		System.out.println("Destination floor: 2");
//		elevator.goDown();
//		try {
//			Thread.sleep(15000);
//			elevator.stop();
//		} catch (InterruptedException e) {
//			System.out.println("Main thread interrupted");
//			e.printStackTrace();
//		}
//    }
	
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
	
	@Override
	public void run() {
		while(true) {
			messageHandler(this.responder.receive());
		}
	}
	
	public void messageHandler(RequestMessage message) {
		int requestType = message.getRequestType();
		boolean sendEmptyResponse = true;
		switch (requestType) {
			case 2001: 
				closeDoor();
				break;
			case 2002:
				openDoor();
				break;
			case 2003:
				stop();
				break;
			case 2004:
				goUp();
				break;
			case 2005:
				goDown();
				break;
			case 2006: 
				toggleLamp(message.getValue());
				break;
			case 2007: 
				pressButton(message.getValue());
				break;
			case 2008:  
				clearButton(message.getValue());
				break;
			case 2009:
				sendEmptyResponse = false; 
				message.sendResponse(new Message(MessageAPI.MSG_CURRENT_FLOOR, currentFloor));
				break;
		}
		
		if(sendEmptyResponse) {
			message.sendResponse(new Message(MessageAPI.MSG_EMPTY_RESPONSE, 0) );
		}
	}
	
	public void stop() {
		this.motor = motorState.STOP;
		if(motorTimerThread.isAlive()) {
			motorTimerThread.interrupt();
			System.out.println("Elevator stopped...");
		}	
	}
	
	public void goUp() {
		this.motor = motorState.UP;
		motorTimerThread = new Thread(new MotorTimer());
		motorTimerThread.start();
		System.out.println("Going up...");
	}
	
	public void goDown() {
		this.motor = motorState.DOWN;
		motorTimerThread = new Thread(new MotorTimer());
		motorTimerThread.start();
		System.out.println("Going down...");
	}
	
	public void openDoor() {
		this.doorOpen = true;
		System.out.println("Door opened");
	}
	
	public void closeDoor() {
		this.doorOpen = false;
		System.out.println("Door closed");
	}
	
	public void toggleLamp(int lampNum) {
		this.lamps[lampNum] = !this.lamps[lampNum];
		System.out.println("Lamp toggled: " + lampNum + ". Value: " + this.lamps[lampNum]);
	}
	
	public void pressButton(int buttonNum) {
		this.buttons[buttonNum] = true;
		System.out.println("Button pressed: " + buttonNum);
	}
	
	public void clearButton(int buttonNum) {
		this.buttons[buttonNum] = false;
		System.out.println("Button cleared: " + buttonNum);
	}
	
	public synchronized void incrementFloor() {
		if(this.currentFloor < this.numberOfFloors) {
			this.currentFloor += 1;
			System.out.println("At floor: " + this.currentFloor);
			floorChangeAlert();
		}
	}
	
	public synchronized void decrementFloor() {
		if(this.currentFloor > 1) {
			this.currentFloor -= 1;
			System.out.println("At floor: " + this.currentFloor);
			floorChangeAlert();
		}
	}
	
	public void floorChangeAlert() {
		try {
			this.requester.sendRequest(InetAddress.getLocalHost(), 8002, new Message(MessageAPI.MSG_CURRENT_FLOOR, currentFloor));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PacketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized int getCurrentFloor() {
		return this.currentFloor;
	}
	
	public boolean isDoorOpen() {
		return this.doorOpen;
	}
	
	public motorState getMotorDirection() {
		return this.motor;
	}
	
	public boolean[] getLamps() {
		return this.lamps;
	}
	
	public boolean[] getButtons() {
		return this.buttons;
	}
	
	class MotorTimer implements Runnable {
		
		@Override
		public void run() {
			while (motor != motorState.STOP) {
				try {
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

