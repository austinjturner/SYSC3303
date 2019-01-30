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

	public enum motor { UP, DOWN, STOP };
	
	private int elevatorId;
	private int currentFloor;
	private boolean doorOpen;
	private boolean buttons[];
	private boolean lamps[];
	private motor motor;
	private Requester requester;
	private Responder responder;
	private Thread timerThread;
	private int numberOfFloors;
	private int addr;
	
	public Elevator(int id, int portNumber, int numberOfFloors) {
		this.requester = new Requester();
		this.responder = new Responder(portNumber);
		this.elevatorId = id;
		this.currentFloor = 1;
		this.numberOfFloors = numberOfFloors;
		this.motor = motor.STOP;
		buttons = new boolean[numberOfFloors];
		lamps = new boolean[numberOfFloors];

	}
	
	public void startElevator() {
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
		this.motor = motor.STOP;
		timerThread.interrupt();
	}
	
	public void goUp() {
		this.motor = motor.UP;
		timerThread = new Thread(this);
		timerThread.start();
	}
	
	public void goDown() {
		this.motor = motor.DOWN;
		timerThread = new Thread(this);
		timerThread.start();
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
	
	public void clearButton(int buttonNum) {
		this.buttons[buttonNum] = false;
	}
	
	public synchronized void incrementFloor() {
		if(this.currentFloor < this.numberOfFloors) {
			this.currentFloor += 1;
			floorChangeAlert();
		}
	}
	
	public synchronized void decrementFloor() {
		if(this.currentFloor > 1) {
			this.currentFloor -= 1;
			floorChangeAlert();
		}
	}
	
	public void floorChangeAlert() {
		try {
			this.requester.sendRequest(InetAddress.getLocalHost(), 8002,(new Message(MessageAPI.MSG_CURRENT_FLOOR, currentFloor)));
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
	
	public motor getMotorDirection() {
		return this.motor;
	}
	
	public boolean[] getLamps() {
		return this.lamps;
	}
	
	public boolean[] getButtons() {
		return this.buttons;
	}

	
	@Override
	public void run() {
		// TODO Implement thread to calculate time taken to change floors
		while (this.motor != motor.STOP) {
			try {
				Thread.sleep(4990);
				if(this.motor == motor.DOWN) {
					this.decrementFloor();
				}
				else if (this.motor == motor.UP) {
					this.incrementFloor();
				}
				System.out.println(this.getCurrentFloor() + " - id: " + Thread.currentThread().getId());
			} catch (InterruptedException e) {
				System.out.println("Stopping elevator...");
				return;
			}
		}
	}
	
	// Testing elevator timer thread, will be removed later
	public static void main(String[] args) {
		Elevator elevator = new Elevator(1, 69, 6);
		System.out.println("Starting elevator...");
		System.out.println("Destination floor: 4");
		System.out.println("Current Floor:\n" + elevator.getCurrentFloor());
		elevator.goUp();
		try {
			Thread.sleep(35000);
			elevator.stop();
		} catch (InterruptedException e) {
			System.out.println("Main thread interrupted");
			e.printStackTrace();
		}
		System.out.println("Destination floor: 2");
		elevator.goDown();
		try {
			Thread.sleep(20000);
			elevator.stop();
		} catch (InterruptedException e) {
			System.out.println("Main thread interrupted");
			e.printStackTrace();
		}
		System.out.println("Current Floor: " + elevator.getCurrentFloor());
    }
}


