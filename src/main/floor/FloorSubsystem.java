/*
 *	Project Title:	Elevator System
 *	Authored by:	Devon Daley
 *	Made For: 		SYSC 3303
 *	Iteration:		1
 * 	Last modified:	January 29th by Devon Daley
*/

package src.main.floor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.io.File;

import src.main.elevator.ElevatorSubsystem;
import src.main.net.MessageAPI.FaultType;
import src.main.scheduler.SchedulerSubsystem;
import src.main.settings.Settings;

/*
FAULTS

0 -> ElevatorFailedToStop
1 -> ElevatorFailedToCloseDoors
2 -> ElevatorFailedToOpenDoors

*/

public class FloorSubsystem {
	
	private inputVar[] msgArray;
	//implement lamps at a later date
	public static boolean[] lampUp;		//on = true
	public static boolean[] lampDown;		//on = true
	private File txtLocation;
	public static FloorSubsystem floorSub;
	
	
	public FloorSubsystem() throws Exception {
		this(Settings.INPUT_FILE_PATH);
	}
	
	public FloorSubsystem(String txtLocation) throws Exception {
		this.txtLocation = new File(txtLocation);
		
		// used to count lines in file for creating msgArray with correct size		
		BufferedReader reader = new BufferedReader(new FileReader(txtLocation));
		int lineCount = 0;
		while (reader.readLine() != null) lineCount++;
		reader.close();
		
		msgArray = new inputVar[lineCount];
		lampUp = new boolean[Settings.NUMBER_OF_FLOORS + 1];	// TODO: I added one to avoid runtime errors
		lampDown = new boolean[Settings.NUMBER_OF_FLOORS + 1];	// TODO: There's probably a better way to fix this
	
		floorSub = this;
	}
	
	private inputVar[] makeInput() throws Exception {
		//Initialize
		BufferedReader br = new BufferedReader(new FileReader(txtLocation));
		
		String line;
		int count = 0;
		int index[] = new int[6];
		int len;
		FaultType type;
		
		//Loop through each line, reading it's contents and making them into a msg array.
		while ((line = br.readLine()) != null ) {
			//Get length
			len = line.length();
			
			//Find the index of all the spaces
			index[0] = line.indexOf(" ");
			index[1] = line.indexOf(" ", index[0]+1);
			index[2] = line.indexOf(" ", index[1]+1);
			index[3] = line.indexOf(" ", index[2]+1);
			index[4] = line.indexOf(" ", index[3]+1);
			index[5] = line.indexOf(" ", index[4]+1);
			
			//Set fault type
			if(Integer.parseInt(line.substring(index[4]+1, index[5])) == 0) {
				type = FaultType.ElevatorFailedToStop;
			} else if (Integer.parseInt(line.substring(index[4]+1, index[5])) == 1) {
				type =  FaultType.ElevatorFailedToCloseDoors;
			} else { // == 2
				type =  FaultType.ElevatorFailedToOpenDoors;
			}
			
			msgArray[count] = new inputVar();
			msgArray[count].setTime(line.substring(0, index[0]));	//Appends time
			msgArray[count].setFloor(Integer.parseInt(line.substring(index[0]+1, index[1])));	//Appends floor
			msgArray[count].setDirection(line.substring(index[1]+1, index[2]));	//Append direction
			msgArray[count].setLength(len);
			
			//Check if a 4th space is found, meaning there are faults
			if(index[3] == -1) {		//if no faults, then use len as last index
				msgArray[count].setDestFloor(Integer.parseInt(line.substring(index[2]+1, len)));	//Appends destFloor
			} else {			//if there are faults
				msgArray[count].setDestFloor(Integer.parseInt(line.substring(index[2]+1, index[3])));	//Appends destFloor
				msgArray[count].setFaultType(type);	//Appends faultType
				msgArray[count].setFaultFloor(Integer.parseInt(line.substring(index[5]+1, len)));		//Appends faultFloor
			}
			
			
			count++;
		}
		br.close();
		
		return msgArray;
	}
	
	//THIS IS LAZY AND INCOMPLETE. NEEDS REVISION!
	private void verifyInput() throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(txtLocation));
		String line = "";
		int index = 0;
		
		while((line = br.readLine()) != null) {
			if((line.length() < 17) & (line.length() > 21)) System.exit(0);		//If its not a valid size, exit
			
			for(int i = 0; i<3; i++) {		//finding the spaces, making sure they are present
				index = line.indexOf(" ");
				if(index == -1) System.exit(0);
				line = line.substring(index+1);
			}
		}
		br.close();
	}
	
	public synchronized void setLamp(int floor, String direction, boolean on) {
		if(direction == "up") {	//If they are going up
			lampUp[floor] = on;
		}
		else {					//If they are going down
			lampDown[floor] = on;
		}
		notifyAll();
	}
	
	public void run() throws Exception{
		//Initialize threads
		FloorSendThread threadSend;
		FloorReceiveThread threadReceive = new FloorReceiveThread();
		
		verifyInput();			//Check that inputs are valid
		inputVar[] inputs = makeInput();			//make an array of messages
		threadSend = new FloorSendThread(inputs);		//Initialize sendThread with array of messages
		threadSend.start();								//start thread
		threadReceive.start();							//start thread
	}
	
	/**
	 * Main method to launch subsystem independently
	 * @param args
	 */
	public static void main(String []args) {
		String testFilePath = "src//main//text//fault_simulation_for_gui_input.txt";
		try {
			FloorSubsystem floor = new FloorSubsystem(testFilePath);
			floor.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}