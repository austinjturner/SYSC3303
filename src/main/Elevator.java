package src.main;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Elevator {

	public enum motor { UP, DOWN, STOP };
	
	private int elevatorId;
	private boolean doorsOpen;
	private boolean buttons[];
	private boolean lamps[];
	private motor motor;
	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket sendSocket, receiveSocket;
	
	public Elevator(int id) {
		this.elevatorId = id;
		doorsOpen = false;
		
		try {
			receiveSocket = new DatagramSocket(69);
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
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
	
	public void openDoors() {
		this.doorsOpen = true;
	}
	
	public void closeDoor() {
		this.doorsOpen = false;
	}
	
	/*
	 * Route button presses to the scheduler
	 * 
	 * @param floorNumber the floor that elevator user pressed
	 */
	public void pushButton(int floorNumber) {
		
	}
	
	
}
