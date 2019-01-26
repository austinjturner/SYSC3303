package src.main.net;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * This class contains common functions and constants for the src.net package
 * 
 * @author austinjturner
 */
public class Common {
	// Number of bytes for int in Java
	public static final int BYTES_PER_INT = 4;
	
	// Convert and int to a byte array
	public static byte[] intToByteArray(int value){
	    return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(value).array();
	}

	// Convert a byte array to an int
	public static int byteArrayToInt(byte[] byteArray){
	    return ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}
	
	// Convert a Message to a byte array
	public static byte[] buildMessageBytes(Message msg) {
		byte[] encodedBytes = new byte[2 * BYTES_PER_INT];

		int index = 0;
		for (byte b : Common.intToByteArray(msg.getRequestType())) {
			encodedBytes[index++] = b;
		}
		for (byte b : Common.intToByteArray(msg.getValue())) {
			encodedBytes[index++] = b;
		}
		return encodedBytes;
	}
	
	// Convert a byte array to a Message
	public static Message bytesToMsg(byte[] b) {
		return new Message(
				Common.byteArrayToInt(new byte[]{b[0], b[1], b[2], b[3]}), 
				Common.byteArrayToInt(new byte[] {b[4], b[5], b[6], b[7]})
		);
	}
}
