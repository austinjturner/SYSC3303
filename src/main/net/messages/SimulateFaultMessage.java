package src.main.net.messages;

import src.main.net.Common;
import src.main.net.MessageAPI;
import src.main.net.MessageAPI.FaultType;

public class SimulateFaultMessage extends Message {
	
	private FaultType faultType;
	private int faultFloorNumber;
	
	private static final int nullFaultType = -1;
	
	
	/**
	 * Constructor for requester
	 * 
	 * @param faultType
	 * @param faultFloorNumber
	 */
	public SimulateFaultMessage(FaultType faultType, int faultFloorNumber) {
		super(MessageAPI.MSG_SIMULATE_FAULT);
		this.faultType = faultType;
		this.faultFloorNumber = faultFloorNumber;
	}
	
	
	/**
	 * Constructor for responder
	 * 
	 * @param msg
	 */
	public SimulateFaultMessage(Message msg) {
		super(MessageAPI.MSG_SIMULATE_FAULT);
		
		this.faultFloorNumber = Common.byteArrayToIntAtIndex(msg.data, 0);
		int enumValue = Common.byteArrayToIntAtIndex(msg.data, 4);
		this.faultType = enumValue == nullFaultType ? null : FaultType.values()[enumValue];
	}
	
	
	/**
	 * Place the 2 data values in the byte array
	 */
	@Override
	public byte[] getData() {
		byte[] data = new byte[8];
		Common.intToByteArrayAtIndex((this.faultType == null ? nullFaultType : this.faultType.ordinal()), data, 0);	// enum value or -1
		Common.intToByteArrayAtIndex(this.faultFloorNumber, data, 4);	
		return data;
	}
	
	
	/**
	 * @return faultType
	 */
	public FaultType getFaultType() {
		return this.faultType;
	}
	
	
	/**
	 * @return faultFloorNumber
	 */
	public int getFaultFloorNumber() {
		return this.faultFloorNumber;
	}
}
