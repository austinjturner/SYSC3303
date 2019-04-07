package src.main.net;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import src.main.net.messages.*;

/**
 * This class contains common functions and constants for the src.net package
 * 
 * @author austinjturner
 */
public class Common {
	
	/*
	 * ==============================================================================
	 *                              Common Constants
	 * ==============================================================================
	 */
	
	// Constant port definitions for subsystems
	public static final int PORT_SCHEDULER_SUBSYSTEM = 8000;
	public static final int PORT_FLOOR_SUBSYSTEM = 8001;
	public static final int PORT_ELEVATOR_SUBSYSTEM = 8002;
	public static InetAddress IP_SCHEDULER_SUBSYSTEM, IP_FLOOR_SUBSYSTEM, IP_ELEVATOR_SUBSYSTEM;
	
	/*
	 * Statically initializing the InetAdddress object for each subsystem
	 * If we get an error here, System.exit(1)
	 * 
	 */
	static {
		try {
			IP_SCHEDULER_SUBSYSTEM = InetAddress.getLocalHost();
			IP_FLOOR_SUBSYSTEM = InetAddress.getLocalHost();
			IP_ELEVATOR_SUBSYSTEM = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	
	// Number of bytes for int in Java
	public static final int BYTES_PER_INT = 4;

	/*
	 * ==============================================================================
	 *                              Common Functions
	 * ==============================================================================
	 */
	
	
	// Convert and int to a byte array
	public static byte[] intToByteArray(int value){
	    return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(value).array();
	}
	
	
	// convert int to byte array, then copy into the provided array at the index
	public static void intToByteArrayAtIndex(int value, byte[] array, int index){
	    byte[] intBytes = intToByteArray(value);
	    for (int i = 0; i < BYTES_PER_INT; i++) {
	    	array[index + i] = intBytes[i];
	    }
	}


	// Convert a byte array to an int
	public static int byteArrayToInt(byte[] byteArray){
		return byteArrayToIntAtIndex(byteArray, 0);
	}
	

	// Convert 4 bytes of an array to an int from the given index
	public static int byteArrayToIntAtIndex(byte[] byteArray, int index){
		byte[] bytes = new byte[BYTES_PER_INT];
		for (int i = 0; i < BYTES_PER_INT; i++) {
			bytes[i] = byteArray[i + index];
		}
	    return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}
	
	
	// Convert a Message to a byte array
	public static byte[] buildMessageBytes(Message msg) {
		byte[] encodedBytes = new byte[BYTES_PER_INT + msg.getData().length];

		int index = 0;
		for (byte b : Common.intToByteArray(msg.getRequestType())) {
			encodedBytes[index++] = b;
		}
		for (byte b : msg.getData()) {
			encodedBytes[index++] = b;
		}
		return encodedBytes;
	}
	
	
	// Convert a byte array to a Message
	public static Message bytesToMsg(byte[] b, int length) {
		byte[] data = new byte[length - Common.BYTES_PER_INT];
		for (int i = 0; i < data.length; i++) {
			data[i] = b[i + Common.BYTES_PER_INT];
		}
		return new Message(
				Common.byteArrayToInt(new byte[]{b[0], b[1], b[2], b[3]}), 
				data
		);
	}
}
