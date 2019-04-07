package src.main.floor;

import java.net.InetAddress;

import src.main.net.*;
import src.main.net.messages.*;
import src.main.settings.Settings;

/**
 * This class represents a thread within the floor subsystem. This thread
 * is responsible for sending messages to the scheduler. These messages are
 * triggered by events parsed from the simulation test file.
 * 
 * It must be synchronized with the rest of the subsystems data structures.
 * 
 * @author Devon
 *
 */
public class FloorSendThread extends Thread{
	
	// Package scoped fields
	inputVar[] msgArray;
	InetAddress address;
	int schedulerPort = Common.PORT_SCHEDULER_SUBSYSTEM;
	int floorPort = Common.PORT_FLOOR_SUBSYSTEM;
	
	// Private field
	private FloorSubsystem system;
	
	/**
	 * Constructor
	 * 
	 * @param msgs to simulate by sending messages to scheduler
	 * @throws Exception
	 */
	public FloorSendThread(inputVar[] msgs) throws Exception{
		msgArray = msgs;
		address = Common.IP_SCHEDULER_SUBSYSTEM;
	}
	
	public void run(){
		system = FloorSubsystem.floorSub;
		Requester requester = new Requester();
		FloorButtonPressMessage message;
		
		for(int i = 0; i < msgArray.length; i++) {
			
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
			try {
				requester.sendRequest(address, schedulerPort, message);
			} catch (PacketException e) {e.printStackTrace();}
		}
		
		requester.close();
	}
	
	
	/**
	 * Handle time in simulation by sleeping between messages.
	 * 
	 * @param hh 	number of hours
	 * @param mm 	number of minutes
	 * @param ss 	number of seconds
	 * @param mmm 	number of millisecond
	 */
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
	
	
	/**
	 * Helper to print lamp state to console.
	 */
	public void printLamps() {
		System.out.println("Floor  Up       Down");
		for(int i = 0; i < Settings.NUMBER_OF_FLOORS; i++) {
			System.out.print(i+1);
			System.out.print("     " + FloorSubsystem.lampUp[i] + "    ");
			System.out.println(FloorSubsystem.lampDown[i]);
			
		}
	}
}
