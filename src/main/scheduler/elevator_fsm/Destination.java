package src.main.scheduler.elevator_fsm;

class Destination {
	public enum DestinationType {
		PICKUP, DROPOFF, PICKUP_AND_DROPOFF, WAIT
	}
	
	public int floorNum;
	public DestinationType destinationType;
	
	public Destination(int floorNum, DestinationType destinationType) {
		this.floorNum = floorNum;
		this.destinationType = destinationType;
	}
}
