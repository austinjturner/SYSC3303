package src.main.floor;

import java.net.InetAddress;

import src.main.net.*;
import src.main.net.messages.*;
import src.main.settings.Settings;

public class FloorSendThread extends Thread{
	
	public inputVar[] msgArray;
	public InetAddress address;
	public int schedulerPort = Common.PORT_SCHEDULER_SUBSYSTEM;
	public int floorPort = Common.PORT_FLOOR_SUBSYSTEM;
	private FloorSubsystem system;
	
	public FloorSendThread(inputVar[] msgs) throws Exception{
		msgArray = msgs;
		address = Common.IP_SCHEDULER_SUBSYSTEM;
	}
	
	public void run(){
		system = FloorSubsystem.floorSub;
		Requester requester = new Requester();
		FloorButtonPressMessage message;
		
		for(int i = 0; i < msgArray.length; i++) {
			
			//printLamps();
			//printInformation(msgArray[i]);
			
			//Make message
			if(msgArray[i].faultFloor == -1) {
				message = new FloorButtonPressMessage(msgArray[i].floor, msgArray[i].destFloor, msgArray[i].direction.equals("up"));
			} else {
				message = new FloorButtonPressMessage(msgArray[i].floor, msgArray[i].destFloor, msgArray[i].direction.equals("up"),
						msgArray[i].faultFloor, msgArray[i].faultType);
			}
			
			//Before sending, delay according to the time-stamp if NOT first time
			if (i != 0) {
				simulateTimestamp(
						msgArray[i].hh - msgArray[i - 1].hh,
						msgArray[i].mm - msgArray[i - 1].mm,
						msgArray[i].ss - msgArray[i - 1].ss,
						msgArray[i].mmm - msgArray[i - 1].mmm);
			}
			

			//set the appropriate lamp
			system.setLamp(msgArray[i].floor, msgArray[i].direction, true);
			
			//send to Scheduler
			//System.out.println("Sending request to Scheduler containing:\n    -Floor that the request is coming from\n    -Request direction\n    -Destination floor\n");
			try {
				requester.sendRequest(address, schedulerPort, message);
			} catch (PacketException e) {e.printStackTrace();}
		}
		
		requester.close();
	}
	
	private void printInformation(inputVar var){
		System.out.print("The information about to be sent to Scheduler: " + var.hh + ":" + var.mm + ":" + var.ss + "." + var.mmm + " " + var.floor + " " + var.direction + " " + var.destFloor + "\n");
	}
	
	public void simulateTimestamp(int hh, int mm, int ss, int mmm){
		try {
			Thread.sleep((long) (Settings.TIME_FACTOR * (
					hh * 3600000 +
					mm * 60000 +
					ss * 1000 +
					mmm)));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void printLamps() {
		System.out.println("Floor  Up       Down");
		for(int i = 0; i < Settings.NUMBER_OF_FLOORS; i++) {
			System.out.print(i+1);
			System.out.print("     " + FloorSubsystem.lampUp[i] + "    ");
			System.out.println(FloorSubsystem.lampDown[i]);
			
		}
	}
}
