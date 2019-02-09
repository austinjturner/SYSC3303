package src.main.net;

/**
 * This class represents a message to transfer that
 * contains an elevatorID
 * 
 * @author austinjturner
 *
 */
public class ElevatorMessage extends Message {

	private int elevatorID, value;
	
	
	/**
	 * Build ElevatorMessage from values
	 * 
	 * @param requestType
	 * @param elevatorID
	 * @param value
	 */
	public ElevatorMessage(int requestType, int elevatorID, int value) {
		super(requestType);
		this.elevatorID = elevatorID;
		this.value = value;
	}
	
	
	/**
	 * Build ElevatorMessage from Message
	 * 
	 * Use a Message instance to parse the elevatorID and value values
	 * Errors will be thrown if the Message is invalid
	 * 
	 * @param msg
	 */
	public ElevatorMessage(Message msg) {
		super(msg.requestType);
		
		// Parse values from msg.data
		this.elevatorID = Common.byteArrayToIntAtIndex(msg.data, 0);
		this.value = Common.byteArrayToIntAtIndex(msg.data, 4);
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
		byte[] data = new byte[8];
		Common.intToByteArrayAtIndex(this.elevatorID, data, 0);
		Common.intToByteArrayAtIndex(this.value, data, 4);	
		return data;
	}
	
	
	/**
	 * @return value
	 */
	@Override
	public int getValue() {
		return this.value;
	}
	
	/**
	 * @return elevatorID
	 */
	public int getElevatorID() {
		return this.elevatorID;
	}
}
