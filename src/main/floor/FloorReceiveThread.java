package src.main.floor;

import java.net.DatagramPacket;
import java.net.InetAddress;

import src.main.net.Common;
import src.main.net.FloorButtonClearMessage;
import src.main.net.Responder;

public class FloorReceiveThread extends Thread {
	
	public inputVar[] msgArray;
	public InetAddress address;
	public int schedulerPort = Common.PORT_SCHEDULER_SUBSYSTEM;
	public int floorPort = Common.PORT_FLOOR_SUBSYSTEM;
	private FloorSubsystem system;
	
	public FloorReceiveThread() {
		
	}
	
	public void run(){
		system = FloorSubsystem.floorSub;
		Responder responder = new Responder(floorPort);
		
		while(true){
			//Initialize
			FloorButtonClearMessage message;
			
			//Receive message to change lamps
			message = new FloorButtonClearMessage(responder.receive());
			
			//Send return message so other systems aren't kept waiting
			DatagramPacket packet = new DatagramPacket(message.getData(), message.getData().length, address, schedulerPort);
			responder.sendResponse(packet, message); 				//The contents of the message don't matter
			
			//turn off specified lamp
			if (message.getGoingUp()==true) {
				system.setLamp(message.getFloorNumber(), "up", false);
			}else {
				system.setLamp(message.getFloorNumber(), "down", false);
			}
		}
	}
}
