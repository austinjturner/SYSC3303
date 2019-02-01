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
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.ByteArrayOutputStream;
import java.io.File;

import src.main.net.Common;

public class FloorSubsystem {
	
	private inputVar[] msgArray;
	private InetAddress address;
	private int schedulerPort;
	private int floorPort;
	//implement lamps at a later date
	private boolean[] lamp;
	private File txtLocation;
	
	
	public FloorSubsystem() throws Exception{
		address = InetAddress.getByName("localhost");
		schedulerPort = 40;
		floorPort = 30;
		msgArray = new inputVar[10];
		lamp = new boolean[5];
		txtLocation = new File("src//main//text//input.txt");
	}
	
	public void run() throws Exception{
		//Verify the validity of all the inputs
		verifyInput();
		
		//Import all inputs into an array
		inputVar[] inputs = makeInput();
		
		//Initialize the stuff we'll be using
		DatagramPacket packet;
		DatagramSocket socket = new DatagramSocket(floorPort);
		ByteArrayOutputStream baos;
		
		for(int i = 0; i < inputs.length; i++) {
			
			printInformation(inputs[i]);
			
			//Initialize
			baos = new ByteArrayOutputStream();		//for appending one byte array onto another
			byte[] b = Common.intToByteArray(inputs[i].floor);		//Find the floor the request is being sent from
			
			//Find the direction it's headed
			byte[] c;
			if(inputs[i].direction.toLowerCase() == "up") {
				c = Common.intToByteArray(1);
			} else {
				c = Common.intToByteArray(0);
			}
			
			//Make message containing the floor the request is coming from and the direction it's going
			baos.write(b);
			baos.write(c);
			byte[] msg = baos.toByteArray();		//the final message
			packet = new DatagramPacket(msg, msg.length, address, schedulerPort);		//make it into a packet
			
			//send to Scheduler
			System.out.println("Sending request to Scheduler containing:\n    -Floor that the request is coming from\n    -Request direction");
			socket.send(packet);
			
			//delay so that we don't receive our own message
			Thread.sleep(10);
			
			//receive packet once an elevator reaches this floor.
			System.out.println("Waiting to receive response from Scheduler saying that elevator is here.");
			socket.receive(packet);
			Thread.sleep(10);
			
			//make a message containing the destination floor
			msg = Common.intToByteArray(inputs[i].destFloor);
			packet = new DatagramPacket(msg, msg.length, address, schedulerPort);
			System.out.println("Sending the destination floor to scheduler...");
			socket.send(packet);
			
			Thread.sleep(20000);		// Delay the messages being sent by 20 seconds
			
		}
		
		socket.close();
		
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
	
	private void printInformation(inputVar var){
		System.out.print("The information about to be send to Scheduler: " + var.hh + ":" + var.mm + ":" + var.ss + "." + var.mmm + " " + var.floor + " " + var.direction + " " + var.destFloor + "\n");
	}
}

//May need to remove the carriage return on String line at some point. Will wait to see if it becomes a problem.
