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
import java.io.File;
import src.main.settings.Settings;

public class FloorSubsystem {
	
	private inputVar[] msgArray;
	//implement lamps at a later date
	public static boolean[] lampUp;		//on = true
	public static boolean[] lampDown;		//on = true
	private File txtLocation;
	public static FloorSubsystem floorSub;
	
	
	public FloorSubsystem() throws Exception{
		txtLocation = new File(Settings.INPUT_FILE_PATH);
		
		// used to count lines in file for creating msgArray with correct size		
		BufferedReader reader = new BufferedReader(new FileReader(txtLocation));
		int lineCount = 0;
		while (reader.readLine() != null) lineCount++;
		reader.close();
		
		msgArray = new inputVar[lineCount];
		lampUp = new boolean[Settings.NUMBER_OF_FLOORS];
		lampDown = new boolean[Settings.NUMBER_OF_FLOORS];
	
		floorSub = this;
	}
	
	private inputVar[] makeInput() throws Exception {
		//Initialize
		BufferedReader br = new BufferedReader(new FileReader(txtLocation));
		
		String line;
		int count = 0;
		int index;
		int index2;
		
		//Loop through each line, reading it's contents and making them into a msg array.
		while ((line = br.readLine()) != null ) {
			index = line.indexOf(" ");	// Find the first space that separates time from floor
			
			msgArray[count] = new inputVar();
			msgArray[count].setTime(line.substring(0, index));	//Appends time
			msgArray[count].setFloor(Integer.parseInt(line.substring(index+1, index+2)));	//Appends floor
			
			index = line.indexOf(" ", index+1);	//Finding where the 2nd space occurs
			index2 = line.indexOf(" ", index+1);	//Finding where the 3rd space occurs
			msgArray[count].setDirection(line.substring(index+1, index2));	//Append direction
			msgArray[count].setDestFloor(Integer.parseInt(line.substring(index2+1, index2+2)));	//Appends destFloor
			msgArray[count].setLength(index2+2);
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

}


/*public void run() throws Exception{
		//Verify the validity of all the inputs
		verifyInput();
		
		//Import all inputs into an array
		inputVar[] inputs = makeInput();
		
		//Initialize the stuff we'll be using
		Requester requester = new Requester(floorPort);
		ByteArrayOutputStream baos;
		
		for(int i = 0; i < inputs.length; i++) {
			
			printInformation(inputs[i]);
			
			//Initialize
			Message message;
			baos = new ByteArrayOutputStream();		//for appending one byte array onto another
			byte[] b = Common.intToByteArray(inputs[i].floor);		//Find the floor the request is being sent from
			
			//Find the direction it's headed
			byte[] c;
			if(inputs[i].direction.toLowerCase() == "up") {
				c = Common.intToByteArray((int) 1);
			} else {
				c = Common.intToByteArray((int) 0);
			}
			
			//Make message containing the floor the request is coming from and the direction it's going
			baos.write(b);
			baos.write(c);
			byte[] msg = baos.toByteArray();		//the final message
			message = new Message(MessageAPI.MSG_ELEVATOR_BUTTON_PRESSED, msg);	//Make message
			
			//TURN LAMP ON
			
			//send to Scheduler
			System.out.println("Sending request to Scheduler containing:\n    -Floor that the request is coming from\n    -Request direction");
			requester.sendRequest(address, schedulerPort, message);
			System.out.println("Elevator is here.");
			
			//TURN LAMP OFF
			
			//make a message containing the destination floor
			msg = Common.intToByteArray(inputs[i].destFloor);
			message = new Message(MessageAPI.MSG_FLOOR_BUTTON_PRESSED, msg);
			System.out.println("Sending the destination floor to scheduler...");
			requester.sendRequest(address, schedulerPort, message);
			
			System.out.println();
			System.out.println("Waiting 10 seconds to not overload the scheduler");
			System.out.println();
		}
		
		requester.close();
	}
 */
