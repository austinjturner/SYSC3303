package src.main.scheduler;

import src.main.net.MessageAPI.FaultType;

/**
 * Represents a destination for an elevator to travel to with the intention type for the destination
 * 
 * @author austinjturner
 */
public class Destination {
	public enum DestinationType {
		PICKUP, DROPOFF, PICKUP_AND_DROPOFF, WAIT
	}
	
	public int floorNum;
	public DestinationType destinationType;
	public FaultType faultType;
	public int faultFloorNumber;
	
	/**
	 * Constructor to build a destination object containing a floor number and destination type.
	 * NO FAULTS
	 * 
	 * @param floorNum Int representing floor number for destination
	 * @param destinationType Enum representing the type/intention of the destination object
	 */
	public Destination(int floorNum, DestinationType destinationType) {
		this(floorNum, destinationType, null, -1);
	}
	
	
	/**
	 * Constructor with all params
	 * 
	 * @param floorNum
	 * @param destinationType
	 * @param hasFault
	 * @param faultFloorNumber
	 */
	public Destination(int floorNum, DestinationType destinationType, 
			FaultType faultType, int faultFloorNumber) {
		this.floorNum = floorNum;
		this.destinationType = destinationType;
		this.faultType = faultType;
		this.faultFloorNumber = faultFloorNumber;
	}
	
	/**
	 * @return boolean representing whether or not this floor message contains a fault
	 */
	public boolean hasFault() {
		return this.faultType != null;
	}
}
