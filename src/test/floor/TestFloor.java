package src.test.floor;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import src.main.net.Common;

public class TestFloor {
	public static void main(String args[]) throws Exception{
		//Initialize
		@SuppressWarnings("resource")
		DatagramSocket socket = new DatagramSocket(Common.PORT_SCHEDULER_SUBSYSTEM);	
		DatagramPacket packet;
		
		while(true) {
			//Reset values
			byte[] msg = new byte[8];
			byte[] tmp = new byte[4];			//for reading just a part of the message
			packet = new DatagramPacket(msg,msg.length);
			
			//Receive message from floor
			System.out.println("Receiving request from floor...");
			socket.receive(packet);
			msg = packet.getData();
		
			//Read the first integer, where the floor is coming from
			for(int i = 0; i < 4; i++) {
				tmp[i] = msg[i];
			}
			System.out.println("The floor that the request is coming from: " + Common.byteArrayToInt(tmp));
		
			//Read the second integer, the direction elevator will go
			for(int i = 0; i < 4; i++) {
			tmp[i] = msg[i+4];
			}
			System.out.println("The direction they're going: " + Common.byteArrayToInt(tmp));
		
			//Send confirmation to Floor
			System.out.println("Sending confirmation to Floor saying the elevator has arrived...");
			Thread.sleep(20);		//wait 20 seconds to make sure floor is listening
			packet = new DatagramPacket(tmp, tmp.length, InetAddress.getByName("localhost"),Common.PORT_FLOOR_SUBSYSTEM);
			socket.send(packet);
			Thread.sleep(10);
		
			// Receive the destfloor
			System.out.println("Receiving destFloor from floor...");
			socket.receive(packet);
			msg = packet.getData();
			System.out.println("The destination floor: " + Common.byteArrayToInt(msg));
			System.out.println();
		}
	}
}
