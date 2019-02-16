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
public class FloorButtonPressMessage extends Message {

	private int pickUpFloorNumber, dropOffFloorNumber;
	private boolean goingUp;
	
	
	/**
	 * Build FloorButtonMessage from values
	 * 
	 * @param floorNumber
	 * @param goingUp
	 */
	public FloorButtonPressMessage(int pickUpFloorNumber, int dropOffFloorNumber, boolean goingUp) {
		super(MessageAPI.MSG_FLOOR_BUTTON_PRESSED);
		this.pickUpFloorNumber = pickUpFloorNumber;
		this.dropOffFloorNumber = dropOffFloorNumber;
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
	public FloorButtonPressMessage(Message msg) {
		super(msg.requestType);
		
		// Parse values from msg.data
		this.goingUp = (msg.data[0] == 1 ? true : false);
		this.pickUpFloorNumber = Common.byteArrayToIntAtIndex(msg.data, 1);
		this.dropOffFloorNumber = Common.byteArrayToIntAtIndex(msg.data, 5);
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
		Common.intToByteArrayAtIndex(this.pickUpFloorNumber, data, 1);	// pick up number
		Common.intToByteArrayAtIndex(this.dropOffFloorNumber, data, 5);	// pick up number		
		return data;
	}
	
	
	/**
	 * @return pickUpFloorNumber
	 */
	public int getPickUpFloorNumber() {
		return this.pickUpFloorNumber;
	}
	
	/**
	 * @return dropOffFloorNumber
	 */
	public int getDropOffFloorNumber() {
		return this.dropOffFloorNumber;
	}
	
	
	/** 
	 * @return goingUp
	 */
	public boolean getGoingUp() {
		return this.goingUp;
	}
}
