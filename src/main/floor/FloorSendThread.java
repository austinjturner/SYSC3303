package src.main.floor;

import java.net.InetAddress;

import src.main.net.Common;
import src.main.net.FloorButtonPressMessage;
import src.main.net.Requester;
import src.main.settings.Settings;
import src.main.net.PacketException;

public class FloorSendThread extends Thread{
	
	public inputVar[] msgArray;
	public InetAddress address;
	public int schedulerPort = Common.PORT_SCHEDULER_SUBSYSTEM;
	public int floorPort = Common.PORT_FLOOR_SUBSYSTEM;
	private FloorSubsystem system;
	
	public FloorSendThread(inputVar[] msgs) throws Exception{
		msgArray = msgs;
		address = InetAddress.getByName("localhost");
	}
	
	public void run(){
		system = FloorSubsystem.floorSub;
		Requester requester = new Requester(floorPort + 111);
		FloorButtonPressMessage message;
		
		for(int i = 0; i < msgArray.length; i++) {
			
			printLamps();
			printInformation(msgArray[i]);
			
			//Make message
			if(msgArray[i].direction == "up") {
				message = new FloorButtonPressMessage(msgArray[i].floor, msgArray[i].destFloor, true);
			} else {
				message = new FloorButtonPressMessage(msgArray[i].floor, msgArray[i].destFloor, false);
			}
			
			//Before sending, delay according to the time-stamp
			simulateTimestamp(msgArray[i].hh,msgArray[i].mm,msgArray[i].ss,msgArray[i].mmm);

			//set the appropriate lamp
			system.setLamp(msgArray[i].floor, msgArray[i].direction, true);
			
			//send to Scheduler
			System.out.println("Sending request to Scheduler containing:\n    -Floor that the request is coming from\n    -Request direction\n    -Destination floor\n");
			try {
				requester.sendRequest(address, schedulerPort, message);
			} catch (PacketException e) {e.printStackTrace();}
		}
		
		requester.close();
	}
	
	private void printInformation(inputVar var){
		System.out.print("The information about to be send to Scheduler: " + var.hh + ":" + var.mm + ":" + var.ss + "." + var.mmm + " " + var.floor + " " + var.direction + " " + var.destFloor + "\n");
	}
	
	public void simulateTimestamp(int hh, int mm, int ss, int mmm){
		try {
			Thread.sleep(hh * 3600000);
			Thread.sleep(mm * 60000);
			Thread.sleep(ss * 1000);
			Thread.sleep(mmm);
		} catch (InterruptedException e) {
			System.exit(0);
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
