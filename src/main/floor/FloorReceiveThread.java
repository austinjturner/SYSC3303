package src.main.floor;

import java.net.InetAddress;

import src.main.net.*;
import src.main.net.messages.*;

/**
 * This class represents a thread within the floor subsystem. This thread
 * is responsible for receiving messages from the scheduler, then
 * taking the appropriate action in response.
 * 
 * It must be synchronized with the rest of the subsystems data structures.
 * 
 * @author Devon
 *
 */
public class FloorReceiveThread extends Thread {
	
	// Package scoped fields for subsystem
	inputVar[] msgArray;
	InetAddress address;
	int schedulerPort = Common.PORT_SCHEDULER_SUBSYSTEM;
	int floorPort = Common.PORT_FLOOR_SUBSYSTEM;
	
	// Private field
	private FloorSubsystem system;
	
	public FloorReceiveThread() {
		address = Common.IP_SCHEDULER_SUBSYSTEM;
	}
	
	public void run(){
		system = FloorSubsystem.floorSub;
		Responder responder = new Responder(floorPort);
		
		while(true){
			//Initialize
			
			//Receive message to change lamps
			RequestMessage rm = responder.receive();
			
			// Get a FloorButtonClearMessage
			FloorButtonClearMessage message = new FloorButtonClearMessage(rm);		
			
			//turn off specified lamp
			if (message.getGoingUp()==true) {
				system.setLamp(message.getFloorNumber(), "up", false);
			} else {
			 	system.setLamp(message.getFloorNumber(), "down", false);
			}
			
			//Send return message so other systems aren't kept waiting
			//The contents of the message don't matter
			rm.sendResponse(new Message(MessageAPI.MSG_EMPTY_RESPONSE));
		}
	}
}
