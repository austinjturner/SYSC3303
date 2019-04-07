package src.main.net.messages;

import src.main.net.Common;
import src.main.net.MessageAPI;

/**
 * This class represents a message to transfer an integer
 * and a boolean between subsystems that represents
 * the floor number, and whether the elevator is going up or down
 * 
 * @author austinjturner
 *
 */
public class FloorButtonClearMessage extends Message {

	private int floorNumber;
	private boolean goingUp;
	
	
	/**
	 * Build FloorButtonMessage from values
	 * 
	 * @param floorNumber
	 * @param goingUp
	 */
	public FloorButtonClearMessage(int floorNumber, boolean goingUp) {
		super(MessageAPI.MSG_CLEAR_FLOOR_BUTTON);
		this.floorNumber = floorNumber;
		this.goingUp = goingUp;
	}
	
	
	/**
	 * Build FloorButtonMessage from Message
	 * 
	 * Use a Message instance to parse the goingUp and floorNum values
	 * Errors will be thrown if the Message is invalid
	 * 
	 * @param msg
	 */
	public FloorButtonClearMessage(Message msg) {
		super(msg.requestType);
		
		// Parse values from msg.data
		this.goingUp = (msg.data[0] == 1 ? true : false);
		this.floorNumber = Common.byteArrayToIntAtIndex(msg.data, 1);
	}
	
	/**
	 * Build FloorButtonClearMessage from Message
	 * 
	 * Use a Message instance to parse the floorNum value.
	 * Errors will be thrown if the Message is invalid
	 * 
	 * @param msg
	 */
	public FloorButtonClearMessage(RequestMessage msg) {
		super(msg.requestType);
		
		// Parse values from msg.data
		this.goingUp = (msg.data[0] == 1 ? true: false);
		this.floorNumber = Common.byteArrayToIntAtIndex(msg.data, 1);
	}
	
	
	/**
	 * Overriding Message getData() to pass custom byte array.
	 * 
	 * 5 bytes of data:
	 * Byte 0 = 0 if this.goingUp == False, else 1
	 * Bytes 1-4 represent the floorNumber
	 */
	@Override
	public byte[] getData() {
		byte[] data = new byte[9];
		data[0] = (byte) (this.goingUp ? 1 : 0);		// byte represent boolean
		Common.intToByteArrayAtIndex(this.floorNumber, data, 1);	// pick up number
		return data;
	}
	
	
	/**
	 * @return floorNumber
	 */
	public int getFloorNumber() {
		return this.floorNumber;
	}
	

	/** 
	 * @return goingUp
	 */
	public boolean getGoingUp() {
		return this.goingUp;
	}
}
