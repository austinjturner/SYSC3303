package src.main.net;

/**
 * A message is the data structure being send via the packets.
 * It contains a requestType, which are defined int MessageAPI.java,
 * as well as an int value which may be used to pass information.
 * 
 * @author austinjturner
 *
 */
public class Message {
	protected int requestType;
	protected byte[] data; 
	
	/**
	 * Create new Message with value
	 * 
	 * @param requestType
	 * @param value
	 */
	public Message(int requestType, int value) {
		this.requestType = requestType;
		this.data = Common.intToByteArray(value);
	}
	
	public Message(int requestType, byte[] value) {
		this.requestType = requestType;
		this.data = value;
	}

	/**
	 * Create new Message without value
	 * 
	 * @param requestType
	 */
	public Message(int requestType) {
		this(requestType, 0);
	}

	/**
	 * @return requestType
	 */
	public int getRequestType() {
		return this.requestType;
	}
	
	/**
	 * @return value
	 */
	public int getValue() {
		return Common.byteArrayToInt(new byte[]{data[0], data[1], data[2], data[3]});
	}
	
	public byte[] getData(){
		return this.data;
	}
}