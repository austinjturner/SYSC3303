package src.main.scheduler;

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
	
	/**
	 * Constructor to build a destination object containing a floor number and destination type.
	 * 
	 * @param floorNum Int representing floor number for destination
	 * @param destinationType Enum representing the type/intention of the destination object
	 */
	public Destination(int floorNum, DestinationType destinationType) {
		this.floorNum = floorNum;
		this.destinationType = destinationType;
	}
}
