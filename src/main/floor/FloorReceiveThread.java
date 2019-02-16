package src.main.floor;

import java.net.InetAddress;

import src.main.net.*;
import src.main.net.messages.*;

public class FloorReceiveThread extends Thread {
	
	public inputVar[] msgArray;
	public InetAddress address;
	public int schedulerPort = Common.PORT_SCHEDULER_SUBSYSTEM;
	public int floorPort = Common.PORT_FLOOR_SUBSYSTEM;
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
